package de.philipphager.disclosure.util.ui.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.R;

public class ScoreView extends LinearLayout {
  @BindView(R.id.scoreIndicator) protected View scoreIndicator;
  @BindView(R.id.scoreTitle) protected TextView scoreTitle;

  public ScoreView(Context context, AttributeSet attrs) {
    super(context, attrs);

    inflate(context, R.layout.view_score, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void setScore(Score score) {
    int color = ContextCompat.getColor(getContext(), score.getColor());

    scoreIndicator.getBackground()
        .setColorFilter(color, PorterDuff.Mode.ADD);
    scoreTitle.setText(score.getText());
    scoreTitle.setTextColor(color);
  }

  public enum Score {
    LOW {
      @Override public int getColor() {
        return R.color.colorScoreLow;
      }

      @Override public String getText() {
        return "no risks";
      }
    }, NORMAL {
      @Override public int getColor() {
        return R.color.colorScoreMedium;
      }

      @Override public String getText() {
        return "normal";
      }
    }, HIGH {
      @Override public int getColor() {
        return R.color.colorScoreHigh;
      }

      @Override public String getText() {
        return "high";
      }
    };

    @ColorRes public abstract int getColor();

    public abstract String getText();
  }
}
