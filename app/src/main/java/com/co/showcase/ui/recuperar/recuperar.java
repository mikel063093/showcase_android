package com.co.showcase.ui.recuperar;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;

import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class recuperar extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edt_email)
    AppCompatEditText edtEmail;
    @Bind(R.id.emailWrapper)
    TextInputLayout emailWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recupear_pass);
        ButterKnife.bind(this);
        renderToolbar();

    }

    private void renderToolbar() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.btn_flechaizquierda);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        });
    }

    @OnClick(R.id.btn_ingresar)
    public void onClick() {
    }
}
