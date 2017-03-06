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

public class PermissionGrantedView extends LinearLayout {
  @BindView(R.id.indicator) protected View indicator;
  @BindView(R.id.title) protected TextView title;

  public PermissionGrantedView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_protection_granted, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void setPermissionGranted(boolean isGranted, boolean canBeRevoked) {
    int color = ContextCompat.getColor(getContext(), getColor(isGranted));

    indicator.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

    StringBuilder status = new StringBuilder(
        String.format(getContext().getString(R.string.permission_status), getIsGranted(isGranted)));

    if (isGranted) {
      status.append(String.format(", %s", getCanBeRevoked(canBeRevoked)));
    }

    title.setText(status);
    title.setTextColor(color);
  }

  private @ColorRes int getColor(boolean isGranted) {
    return isGranted ? R.color.color_text_warning : R.color.color_text_primary;
  }

  private String getIsGranted(boolean isGranted) {
    int id = isGranted
        ? R.string.permission_status_granted
        : R.string.permission_status_not_granted;

    return getContext().getString(id);
  }

  private String getCanBeRevoked(boolean canBeRevoked) {
    int id = canBeRevoked
        ? R.string.permission_status_can_be_revoked
        : R.string.permission_status_cannot_be_revoked;

    return getContext().getString(id);
  }
}
