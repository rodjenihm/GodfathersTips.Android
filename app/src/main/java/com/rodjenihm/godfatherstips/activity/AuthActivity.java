package com.rodjenihm.godfatherstips.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.Utilities;
import com.rodjenihm.godfatherstips.fragment.AboutFragment;
import com.rodjenihm.godfatherstips.fragment.ContactFragment;
import com.rodjenihm.godfatherstips.fragment.HomeFragment;
import com.rodjenihm.godfatherstips.fragment.ResetPasswordFragment;
import com.rodjenihm.godfatherstips.fragment.SignInFragment;
import com.rodjenihm.godfatherstips.fragment.SignUpFragment;
import com.rodjenihm.godfatherstips.model.AppUser;

import java.util.List;

public class AuthActivity extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private DrawerBuilder drawerBuilderGuest = null;
    private DrawerBuilder drawerBuilderMember = null;

    private Drawer drawer = null;

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

        drawerBuilderGuest = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.colorBackground));
        addCommonDrawerItems(drawerBuilderGuest);
        drawerBuilderGuest.addDrawerItems(new DividerDrawerItem());
        addAuthDrawerItems(drawerBuilderGuest);
        drawerBuilderGuest.addDrawerItems(new DividerDrawerItem());

        drawerBuilderMember = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.colorBackground));
        addCommonDrawerItems(drawerBuilderMember);
        drawerBuilderMember.addDrawerItems(new DividerDrawerItem());
        addSignOutDrawerItem(drawerBuilderMember);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isSignedIn = currentUser != null;

        if (!isSignedIn) {
            drawer = drawerBuilderGuest.build();
            setFragment(SignInFragment.class);
            dlg.dismiss();
        } else {
            firestore
                    .collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        AppUser user = documentSnapshot.toObject(AppUser.class);
                        List<String> roles = user.getRoles();

                        drawer = drawerBuilderMember.build();
                        setFragment(HomeFragment.class);
                        dlg.dismiss();
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

    private void addCommonDrawerItems(DrawerBuilder drawer) {
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem()
                .withName(R.string.drawer_item_home)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withIcon(getResources().getDrawable(R.drawable.ic_home))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(HomeFragment.class));

        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem()
                .withName(R.string.drawer_item_about)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withIcon(getResources().getDrawable(R.drawable.about_icon))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(AboutFragment.class));

        PrimaryDrawerItem itemContact = new PrimaryDrawerItem()
                .withIdentifier(7)
                .withName(R.string.drawer_item_contact)
                .withIcon(getResources().getDrawable(android.R.drawable.ic_dialog_email))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(ContactFragment.class));

        PrimaryDrawerItem itemShare = new PrimaryDrawerItem()
                .withName(R.string.drawer_item_share)
                .withIcon(getResources().getDrawable(R.drawable.share_icon))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> showShareMenu());

        drawer.addDrawerItems(itemHome, itemAbout, itemContact, itemShare);
    }

    private void addAuthDrawerItems(DrawerBuilder drawer) {
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

        drawer.addDrawerItems(itemSignIn, itemSignUp, itemResetPassword);
    }

    private void addSignOutDrawerItem(DrawerBuilder drawerBuilder) {
        PrimaryDrawerItem itemSignOut =
                new PrimaryDrawerItem()
                        .withName(R.string.sign_out)
                        .withIcon(R.drawable.sign_out_icon)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            mAuth.signOut();
                            recreate();
                            drawer.closeDrawer();
                            return true;
                        });

        drawerBuilder.addDrawerItems(itemSignOut);
    }

    private boolean setFragment(Class fragmentClass) {
        try {
            Fragment fragment = (Fragment)fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            drawer.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean showShareMenu() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Postanite deo Godfathers zajednice i uverite zasto smo najbolji {link ka aplikaciji}";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share subject");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        drawer.closeDrawer();
        return true;
    }
}
