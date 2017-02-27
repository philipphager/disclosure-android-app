package de.philipphager.disclosure.util.ui.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.R;

public class ProtectionLevelView extends LinearLayout {
  @BindView(R.id.levelIndicator) protected View protectionLevel;
  @BindView(R.id.levelTitle) protected TextView protectionLevelTitle;

  public ProtectionLevelView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_protection_level, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void setProtectionLevel(ProtectionLevel protectionLevel) {
    int color = ContextCompat.getColor(getContext(), protectionLevel.getColor());

    this.protectionLevel.getBackground()
        .setColorFilter(color, PorterDuff.Mode.ADD);
    protectionLevelTitle.setText(protectionLevel.getText());
    protectionLevelTitle.setTextColor(color);
  }

  public enum ProtectionLevel {
    NORMAL {
      @Override public int getColor() {
        return R.color.colorProtectionNormal;
      }

      @Override public String getText() {
        return "Normal";
      }
    }, DANGEROUS {
      @Override public int getColor() {
        return R.color.colorProtectionDangerous;
      }

      @Override public String getText() {
        return "Dangerous";
      }
    }, SIGNATURE {
      @Override public int getColor() {
        return R.color.colorProtectionSignature;
      }

      @Override public String getText() {
        return "Signature";
      }
    };

    @ColorRes public abstract int getColor();

    public abstract String getText();
  }
}
