package com.rodjenihm.godfatherstips.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.fragment.AboutFragment;
import com.rodjenihm.godfatherstips.fragment.AddTipFragment;
import com.rodjenihm.godfatherstips.fragment.ContactFragment;
import com.rodjenihm.godfatherstips.fragment.HomeFragment;
import com.rodjenihm.godfatherstips.fragment.TipFragment;
import com.rodjenihm.godfatherstips.fragment.UserFragment;
import com.rodjenihm.godfatherstips.model.AppUser;
import com.rodjenihm.godfatherstips.model.Tip;

public class NavigationActivity extends AppCompatActivity implements TipFragment.OnListFragmentInteractionListener, UserFragment.OnListFragmentInteractionListener {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private Drawer drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        AppUser user = (AppUser)getIntent().getSerializableExtra("user");
        boolean isVip = user.getRoles().contains("VIP");
        boolean isAdmin = user.getRoles().contains("ADMIN");
        drawer = buildContentMaterialDrawer(user, isVip, isAdmin);
    }

    private Drawer buildContentMaterialDrawer(AppUser user, boolean isVip, boolean isAdmin) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(user.getEmail())
                                .withEmail(user.getEmail())
                                .withIcon(getResources().getDrawable(R.drawable.profile_icon))
                )
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();

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
                .withName(R.string.drawer_item_contact)
                .withIcon(getResources().getDrawable(android.R.drawable.ic_dialog_email))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(ContactFragment.class));

        PrimaryDrawerItem itemShare = new PrimaryDrawerItem()
                .withName(R.string.drawer_item_share)
                .withIcon(getResources().getDrawable(R.drawable.share_icon))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> showShareMenu());

        SecondaryDrawerItem itemTipsHot = new SecondaryDrawerItem()
                .withEnabled(isVip || isAdmin)
                .withLevel(4)
                .withName(R.string.drawer_item_tips_hot)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    try {
                        Fragment fragment = TipFragment.class.newInstance().withActive(true);
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        drawer.closeDrawer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                });

        SecondaryDrawerItem itemTipsHistory = new SecondaryDrawerItem()
                .withEnabled(isVip || isAdmin)
                .withLevel(4)
                .withName(R.string.drawer_item_tips_history)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    try {
                        Fragment fragment = TipFragment.class.newInstance().withActive(false);
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        drawer.closeDrawer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                });

        SecondaryDrawerItem itemTipsAdd = new SecondaryDrawerItem()
                .withEnabled(isAdmin)
                .withLevel(4)
                .withName(R.string.drawer_item_tips_add)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(AddTipFragment.class));

        ExpandableDrawerItem itemTips = new ExpandableDrawerItem()
                .withEnabled(isVip || isAdmin)
                .withName(R.string.drawer_item_tips)
                .withTextColor(getResources().getColor(R.color.colorText))
                .withIcon(getResources().getDrawable(R.drawable.tip_icon))
                .withArrowColor(getResources().getColor(R.color.colorText))
                .withSubItems(itemTipsHot, itemTipsHistory, itemTipsAdd);

        PrimaryDrawerItem itemChat = new PrimaryDrawerItem()
                .withEnabled(isVip || isAdmin)
                .withName(R.string.drawer_item_chat)
                .withIcon(getResources().getDrawable(R.drawable.chat_icon))
                .withTextColor(getResources().getColor(R.color.colorText));

        PrimaryDrawerItem itemUsers = new PrimaryDrawerItem()
                .withEnabled(isAdmin)
                .withName(R.string.drawer_item_users)
                .withIcon(getResources().getDrawable(R.drawable.users_icon))
                .withTextColor(getResources().getColor(R.color.colorText))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> setFragment(UserFragment.class));

        PrimaryDrawerItem itemSignOut =
                new PrimaryDrawerItem()
                        .withName(R.string.sign_out)
                        .withIcon(R.drawable.sign_out_icon)
                        .withTextColor(getResources().getColor(R.color.colorText))
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            mAuth.signOut();
                            startActivity(new Intent(this, AuthActivity.class));
                            finish();
                            return true;
                        });

        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(itemHome, itemAbout, itemContact, itemShare,
                        new DividerDrawerItem(),
                        itemTips, itemChat, itemUsers,
                        new DividerDrawerItem(),
                        itemSignOut)
                .withSliderBackgroundColor(getResources().getColor(R.color.colorBackground));

        return drawerBuilder.build();
    }

    private boolean setFragment(Class fragmentClass) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
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

    @Override
    public void onListFragmentInteraction(Tip item) {

    }

    @Override
    public void onListFragmentInteraction(AppUser item) {

    }
}
