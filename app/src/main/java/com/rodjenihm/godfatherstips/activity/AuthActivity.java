package com.rodjenihm.godfatherstips.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.Utilities;
import com.rodjenihm.godfatherstips.fragment.ResetPasswordFragment;
import com.rodjenihm.godfatherstips.fragment.SignInFragment;
import com.rodjenihm.godfatherstips.fragment.SignUpFragment;
import com.rodjenihm.godfatherstips.fragment.TipFragment;
import com.rodjenihm.godfatherstips.fragment.UserFragment;
import com.rodjenihm.godfatherstips.model.AppUser;
import com.rodjenihm.godfatherstips.model.Tip;

public class AuthActivity extends AppCompatActivity implements TipFragment.OnListFragmentInteractionListener, UserFragment.OnListFragmentInteractionListener {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private Drawer drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            drawer = buildAuthMaterialDrawer();
            setFragment(SignInFragment.class);
        } else {
            final ProgressDialog dlg = new ProgressDialog(this);
            dlg.setTitle(R.string.please_wait);
            dlg.setMessage(getResources().getString(R.string.loading_user_data));
            dlg.show();

            db.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        dlg.dismiss();
                        AppUser user = documentSnapshot.toObject(AppUser.class);
                        Intent intent = new Intent(this, NavigationActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        dlg.dismiss();
                        Utilities.showAlertDialog(
                                this,
                                getResources().getString(R.string.loading_user_data_error),
                                e.getLocalizedMessage(),
                                mAuth::signOut);
                        recreate();
                    });
        }
    }

    private Drawer buildAuthMaterialDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.colorBackground));

        PrimaryDrawerItem itemSignIn =
                new PrimaryDrawerItem()
                        .withName(R.string.sign_in)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(SignInFragment.class));

        PrimaryDrawerItem itemSignUp =
                new PrimaryDrawerItem()
                        .withName(R.string.sign_up_with_email)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(SignUpFragment.class));

        PrimaryDrawerItem itemResetPassword =
                new PrimaryDrawerItem()
                        .withName(R.string.reset_password)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(ResetPasswordFragment.class));

        drawerBuilder.addDrawerItems(itemSignIn, itemSignUp, itemResetPassword);

        return drawerBuilder.build();
    }

    private boolean setFragment(Class fragmentClass) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.flAuth, fragment).commit();
            drawer.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onListFragmentInteraction(Tip item) {

    }

    @Override
    public void onListFragmentInteraction(AppUser item) {

    }
}
