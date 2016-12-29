package com.wingsofts.wingue.wowsplash.support;

import android.graphics.Path;
import android.graphics.PointF;

public class SvgPathParser {

  private static final int TOKEN_ABSOLUTE_COMMAND = 1;
  private static final int TOKEN_RELATIVE_COMMAND = 2;
  // 具体的数值或".""-"
  private static final int TOKEN_VALUE = 3;
  private static final int TOKEN_EOF = 4;

  private int mCurrentToken;
  private PointF mCurrentPoint = new PointF();
  private int mLength;
  private int mIndex;
  // 要解析的path 字符串
  private String mPathString;

  protected float transformX(float x) {
    return x;
  }

  protected float transformY(float y) {
    return y;
  }

  public Path parsePath(String s) {
    try {
      mCurrentPoint.set(Float.NaN, Float.NaN);
      mPathString = s;
      mIndex = 0;
      mLength = mPathString.length();

      PointF tempPoint1 = new PointF();
      PointF tempPoint2 = new PointF();
      PointF tempPoint3 = new PointF();

      Path p = new Path();
      p.setFillType(Path.FillType.WINDING);

      boolean firstMove = true;
      while (mIndex < mLength) {
        char command = consumeCommand();
        boolean relative = (mCurrentToken == TOKEN_RELATIVE_COMMAND);
        switch (command) {
          case 'M':
          case 'm': {
            // m指令，相当于android 里的 moveTo()
            boolean firstPoint = true;
            while (advanceToNextToken() == TOKEN_VALUE) {
              consumeAndTransformPoint(tempPoint1, relative && mCurrentPoint.x != Float.NaN);
              if (firstPoint) {
                p.moveTo(tempPoint1.x, tempPoint1.y);
                firstPoint = false;
                if (firstMove) {
                  mCurrentPoint.set(tempPoint1);
                  firstMove = false;
                }
              } else {
                p.lineTo(tempPoint1.x, tempPoint1.y);
              }
            }
            mCurrentPoint.set(tempPoint1);
            break;
          }

          case 'C':
          case 'c': {
            // c指令，相当于android 里的 cubicTo()
            if (mCurrentPoint.x == Float.NaN) {
              throw new Exception("Relative commands require current point");
            }

            while (advanceToNextToken() == TOKEN_VALUE) {
              consumeAndTransformPoint(tempPoint1, relative);
              consumeAndTransformPoint(tempPoint2, relative);
              consumeAndTransformPoint(tempPoint3, relative);
              p.cubicTo(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y, tempPoint3.x,
                  tempPoint3.y);
            }
            mCurrentPoint.set(tempPoint3);
            break;
          }

          case 'L':
          case 'l': {
            // 相当于lineTo()进行画直线
            if (mCurrentPoint.x == Float.NaN) {
              throw new Exception("Relative commands require current point");
            }

            while (advanceToNextToken() == TOKEN_VALUE) {
              consumeAndTransformPoint(tempPoint1, relative);
              p.lineTo(tempPoint1.x, tempPoint1.y);
            }
            mCurrentPoint.set(tempPoint1);
            break;
          }

          case 'H':
          case 'h': {
            // 画水平直线
            if (mCurrentPoint.x == Float.NaN) {
              throw new Exception("Relative commands require current point");
            }

            while (advanceToNextToken() == TOKEN_VALUE) {
              float x = transformX(consumeValue());
              if (relative) {
                x += mCurrentPoint.x;
              }
              p.lineTo(x, mCurrentPoint.y);
            }
            mCurrentPoint.set(tempPoint1);
            break;
          }

          case 'V':
          case 'v': {
            // 画竖直直线
            if (mCurrentPoint.x == Float.NaN) {
              throw new Exception("Relative commands require current point");
            }

            while (advanceToNextToken() == TOKEN_VALUE) {
              float y = transformY(consumeValue());
              if (relative) {
                y += mCurrentPoint.y;
              }
              p.lineTo(mCurrentPoint.x, y);
            }
            mCurrentPoint.set(tempPoint1);
            break;
          }

          case 'Z':
          case 'z': {
            // 封闭path
            p.close();
            break;
          }
        }
      }

      return p;
    } catch (Exception e) {

    }
    return null;
  }

  private int advanceToNextToken() {
    while (mIndex < mLength) {
      char c = mPathString.charAt(mIndex);
      if ('a' <= c && c <= 'z') {
        return (mCurrentToken = TOKEN_RELATIVE_COMMAND);
      } else if ('A' <= c && c <= 'Z') {
        return (mCurrentToken = TOKEN_ABSOLUTE_COMMAND);
      } else if (('0' <= c && c <= '9') || c == '.' || c == '-') {
        return (mCurrentToken = TOKEN_VALUE);
      }

      ++mIndex;
    }

    return (mCurrentToken = TOKEN_EOF);
  }

  private char consumeCommand() throws Exception {
    advanceToNextToken();
    if (mCurrentToken != TOKEN_RELATIVE_COMMAND && mCurrentToken != TOKEN_ABSOLUTE_COMMAND) {
      throw new Exception("Expected command");
    }

    return mPathString.charAt(mIndex++);
  }

  private void consumeAndTransformPoint(PointF out, boolean relative) throws Exception {
    out.x = transformX(consumeValue());
    out.y = transformY(consumeValue());
    if (relative) {
      out.x += mCurrentPoint.x;
      out.y += mCurrentPoint.y;
    }
  }

  private float consumeValue() throws Exception {
    advanceToNextToken();
    if (mCurrentToken != TOKEN_VALUE) {
      throw new Exception("Expected value");
    }

    boolean start = true;
    boolean seenDot = false;
    int index = mIndex;
    while (index < mLength) {
      char c = mPathString.charAt(index);
      if (!('0' <= c && c <= '9') && (c != '.' || seenDot) && (c != '-' || !start)) {
        break;
      }
      if (c == '.') {
        seenDot = true;
      }
      start = false;
      ++index;
    }

    if (index == mIndex) {
      throw new Exception("Expected value");
    }

    String str = mPathString.substring(mIndex, index);
    try {
      float value = Float.parseFloat(str);
      mIndex = index;
      return value;
    } catch (NumberFormatException e) {
      throw new Exception("Invalid float value '" + str + "'.");
    }
  }
}
