package com.rodjenihm.godfatherstips.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.Utilities;
import com.rodjenihm.godfatherstips.fragment.AboutFragment;
import com.rodjenihm.godfatherstips.fragment.ContactFragment;
import com.rodjenihm.godfatherstips.fragment.HomeFragment;
import com.rodjenihm.godfatherstips.fragment.ResetPasswordFragment;
import com.rodjenihm.godfatherstips.fragment.SignInFragment;
import com.rodjenihm.godfatherstips.fragment.SignUpFragment;
import com.rodjenihm.godfatherstips.model.AppUser;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setTitle(R.string.please_wait);
        dlg.setMessage(getResources().getString(R.string.loading_data));
        dlg.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.colorBackground))
                .build();
        
        addCommonDrawerItems();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isSignedIn = currentUser != null;
        if (!isSignedIn) {
            dlg.dismiss();
            result.addItem(getAuthItems(isSignedIn));
        } else {
            firestore
                    .collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        dlg.dismiss();
                        AppUser user = documentSnapshot.toObject(AppUser.class);
                    })
                    .addOnFailureListener(e -> {
                        dlg.dismiss();
                        Utilities.showAlertDialog(
                                this,
                                getResources().getString(R.string.loading_data_error),
                                e.getLocalizedMessage(),
                                mAuth::signOut);
                    });
        }
    }

    private void addCommonDrawerItems() {
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.drawer_item_home)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withIcon(getResources().getDrawable(R.drawable.ic_home))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem(1));

        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.drawer_item_about)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withIcon(getResources().getDrawable(R.drawable.about_icon))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem(2));

        PrimaryDrawerItem itemContact = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.drawer_item_contact)
                .withIcon(getResources().getDrawable(android.R.drawable.ic_dialog_email))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem(3));

        PrimaryDrawerItem itemShare = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.drawer_item_share)
                .withIcon(getResources().getDrawable(R.drawable.share_icon))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Postanite deo Godfathers zajednice i uverite zasto smo najbolji {link ka aplikaciji}";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share subject");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    return true;
                });
    }

    private IDrawerItem getAuthItems(boolean isSignedIn) {
        ExpandableDrawerItem identity =
                new ExpandableDrawerItem()
                .withName(R.string.identity)
                .withTextColor(getResources().getColor(R.color.colorText));

        PrimaryDrawerItem itemSignIn =
                new PrimaryDrawerItem()
                        .withIdentifier(1)
                        .withName(R.string.sign_in)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem((int) drawerItem.getIdentifier()));

        PrimaryDrawerItem itemSignUp =
                new PrimaryDrawerItem()
                        .withIdentifier(2)
                        .withName(R.string.sign_up_with_email)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem((int) drawerItem.getIdentifier()));

        PrimaryDrawerItem itemResetPassword =
                new PrimaryDrawerItem()
                        .withIdentifier(3)
                        .withName(R.string.reset_password)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem((int) drawerItem.getIdentifier()));

        PrimaryDrawerItem itemSignOut =
                new PrimaryDrawerItem()
                        .withName(R.string.sign_out)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> selectDrawerItem((int) drawerItem.getIdentifier()))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            mAuth.signOut();
                            Activity currentActivity = this;
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            return true;
                        });

        if(isSignedIn) {
            identity.withSubItems(itemSignOut);
        } else {
            identity.withSubItems(itemSignIn, itemSignUp, itemResetPassword);
        }

        return identity;
    }

    private boolean selectDrawerItem(int ItemId) {
        Class fragmentClass = SignInFragment.class;

        switch (ItemId) {
            case 1:
                fragmentClass = HomeFragment.class;
                break;
            case 2:
                fragmentClass = AboutFragment.class;
                break;
            case 3:
                fragmentClass = ContactFragment.class;
                break;

            case 7:
                fragmentClass = SignInFragment.class;
                break;
            case 8:
                fragmentClass = SignUpFragment.class;
                break;
            case 9:
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
