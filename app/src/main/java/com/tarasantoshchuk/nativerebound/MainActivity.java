package com.tarasantoshchuk.nativerebound;
import android.animation.Animator;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.tarasantoshchuk.nativerebound.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    private ActivityMainBinding mBinding;
    private boolean isAnimated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding =  DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.animationControl.setText(R.string.start_animation);
        mBinding.animationControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnimated) {
                    mBinding.animationControl.setText(R.string.start_animation);
                    mBinding.bouncingView.setTranslationY(0);
                    mBinding.springBouncingView.setTranslationY(0);
                    isAnimated = false;
                } else {
                    mBinding.animationControl.setText(R.string.reset);

                    int friction = Integer.valueOf(mBinding.frictionEt.getText().toString());
                    int tension = Integer.valueOf(mBinding.tensionEt.getText().toString());

                    int translation = mBinding.tensionEt.getTop() / 2;

                    startSpringAnimation(translation, friction, tension);
                    startNativeAnimation(translation, friction, tension);
                    isAnimated = true;
                }
            }
        });
    }

    private void startNativeAnimation(int translation, int friction, int tension) {
        NativeReboundInterpolator interpolator = new NativeReboundInterpolator(tension, friction);
        mBinding.bouncingView
                .animate()
                .setDuration(interpolator.getNaturalDuration())
                .translationY(translation)
                .setInterpolator(interpolator)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Toast.makeText(MainActivity.this, "native animation end", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    private void startSpringAnimation(final int translation, int friction, int tension) {
        SpringSystem system = SpringSystem.create();
        Spring spring = system.createSpring();
        spring.setSpringConfig(new SpringConfig(tension, friction));
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();

                mBinding.springBouncingView.setTranslationY((float) (value * translation));
            }
        });
        spring.setEndValue(1);
    }
}
