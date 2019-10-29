package com.rodjenihm.godfatherstips.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.fragment.ResetPasswordFragment;
import com.rodjenihm.godfatherstips.fragment.SignInFragment;
import com.rodjenihm.godfatherstips.fragment.SignUpFragment;

public class AuthActivity extends AppCompatActivity {
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            configureAuthMaterialDrawer(toolbar);
            selectAuthItem(1);
        } else {

        }

    }

    private void configureAuthMaterialDrawer(Toolbar toolbar) {
        PrimaryDrawerItem itemSignIn =
                new PrimaryDrawerItem()
                        .withIdentifier(1)
                        .withName(R.string.sign_in)
                        .withTextColor(getResources().getColor(R.color.colorText));

        PrimaryDrawerItem itemSignUp =
                new PrimaryDrawerItem()
                        .withIdentifier(2)
                        .withName(R.string.sign_up_with_email)
                        .withTextColor(getResources().getColor(R.color.colorText));

        PrimaryDrawerItem itemResetPassword =
                new PrimaryDrawerItem()
                        .withIdentifier(3)
                        .withName(R.string.reset_password)
                        .withTextColor(getResources().getColor(R.color.colorText));

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        itemSignIn,
                        new DividerDrawerItem(),
                        itemSignUp,
                        new DividerDrawerItem(),
                        itemResetPassword)
                .withSliderBackgroundColor(getResources().getColor(R.color.colorBackground))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> selectAuthItem(((int) drawerItem.getIdentifier())))
                .build();
    }

    private boolean selectAuthItem(int ItemId) {
        Class fragmentClass = SignInFragment.class;

        switch (ItemId) {
            case 1:
                fragmentClass = SignInFragment.class;
                break;
            case 2:
                fragmentClass = SignUpFragment.class;
                break;
            case 3:
                fragmentClass = ResetPasswordFragment.class;
                break;
        }
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            result.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
