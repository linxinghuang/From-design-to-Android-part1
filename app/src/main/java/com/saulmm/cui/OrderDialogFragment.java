package com.saulmm.cui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saulmm.cui.databinding.FragmentOrderFormBinding;
import com.saulmm.cui.databinding.LayoutFormOrderStep1Binding;

import de.hdodenhof.circleimageview.CircleImageView;


public class OrderDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private FragmentOrderFormBinding binding;
    private Transition selectedViewTransition;

    public static OrderDialogFragment newInstance() {
        return new OrderDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        binding = FragmentOrderFormBinding.inflate(LayoutInflater.from(getContext()));

        selectedViewTransition = TransitionInflater.from(getContext())
            .inflateTransition(R.transition.move);


        final View contentView = binding.getRoot();
        dialog.setContentView(contentView);

        configureListeners(binding.layoutStep1);
    }

    public void onClick(View v) {
        v.setSelected(true);
        v.setVisibility(View.INVISIBLE);

        final View selectedView = (v instanceof TextView)
            ? createFakeSelectedSizeView((TextView) v)
            : createFakeSelectedColorView((ImageView) v);

        ((ViewGroup) binding.getRoot()).addView(selectedView);

        startTransition(selectedView);
    }

    private void startTransition(View selectedView) {
        selectedView.post(() -> {
            if (getActivity() != null) {
                TransitionManager.beginDelayedTransition(
                    (ViewGroup) binding.getRoot(), selectedViewTransition);

                selectedView.setLayoutParams(createFakeEndParams(selectedView)); // Fire the transition
            }
        });
    }

    private View createFakeSelectedSizeView(TextView textView) {
        final TextView fakeSelectedView = new TextView(
            getContext(), null, R.attr.sizeStyle);

        fakeSelectedView.setText(textView.getText());
        fakeSelectedView.setLayoutParams(createFakeInitParams(textView));
        fakeSelectedView.setSelected(true);
        return fakeSelectedView;
    }

    private View createFakeSelectedColorView(ImageView imageView) {
        final ImageView fakeImageView = new CircleImageView(
            getContext(), null, R.attr.colorStyle);

        fakeImageView.setImageDrawable(imageView.getDrawable());
        fakeImageView.setLayoutParams(createFakeInitParams(imageView));
        return fakeImageView;
    }

    private ConstraintLayout.LayoutParams createFakeEndParams(View v) {
        final ConstraintLayout.LayoutParams layoutParams =
            (ConstraintLayout.LayoutParams) v.getLayoutParams();

        int marginLeft = getContext().getResources()
            .getDimensionPixelOffset(R.dimen.spacing_medium);

        layoutParams.setMargins(marginLeft, 0, 0, 0);

        if (v instanceof TextView)
            setParamsForSize(layoutParams);
        else
            setParamsForColor(layoutParams);

        return layoutParams;
    }

    private void setParamsForColor(ConstraintLayout.LayoutParams layoutParams) {
        layoutParams.topToTop = binding.txtLabelColour.getId();
        layoutParams.startToEnd = binding.txtLabelColour.getId();
        layoutParams.bottomToBottom = binding.txtLabelColour.getId();
    }

    private void setParamsForSize(ConstraintLayout.LayoutParams layoutParams) {
        layoutParams.topToTop = binding.txtLabelSize.getId();
        layoutParams.startToEnd = binding.txtLabelSize.getId();
        layoutParams.baselineToBaseline = binding.txtLabelSize.getId();
    }

    private ConstraintLayout.LayoutParams createFakeInitParams(View v) {
        final int selectedViewSize = getResources().getDimensionPixelSize(R.dimen.product_color_size);

        final ConstraintLayout.LayoutParams layoutParams =
            new ConstraintLayout.LayoutParams(selectedViewSize, selectedViewSize);

        layoutParams.topToTop = binding.formContainer.getId();
        layoutParams.leftToLeft = binding.formContainer.getId();
        layoutParams.setMargins((int) v.getX(), (int) v.getY(), 0, 0);
        return layoutParams;
    }

    private void configureListeners(LayoutFormOrderStep1Binding layoutStep1) {
        layoutStep1.txt1.setOnClickListener(this);
        layoutStep1.txt2.setOnClickListener(this);
        layoutStep1.txt3.setOnClickListener(this);
        layoutStep1.txt4.setOnClickListener(this);
        layoutStep1.txt5.setOnClickListener(this);

        layoutStep1.imgColorBlue.setOnClickListener(this);
        layoutStep1.imgColorGreen.setOnClickListener(this);
        layoutStep1.imgColorPurple.setOnClickListener(this);
        layoutStep1.imgColorRed.setOnClickListener(this);
        layoutStep1.imgColorYellow.setOnClickListener(this);
    }
}
