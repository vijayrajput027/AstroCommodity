package com.mohanastrology.commodity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mohanastrology.commodity.Fragment.HomeFragment;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try{
            LoginActivity.fa.finish();
        }catch(Exception e){

        }
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        drawerFragment.addNavItem("Home", R.drawable.home);
        drawerFragment.addNavItem("Commodity", R.drawable.cricket);
        drawerFragment.addNavItem("Currency", R.drawable.plitics);
        drawerFragment.addNavItem("RashiPhal", R.drawable.politician);
        drawerFragment.addNavItem("Problem & Solution", R.drawable.politician);
        drawerFragment.addNavItem("Prediction", R.drawable.politician);
        drawerFragment.addNavItem("Luck Improvement Tips", R.drawable.politician);
        drawerFragment.addNavItem("Numerology Predictions", R.drawable.politician);
        drawerFragment.addNavItem("Vastu Tips", R.drawable.politician);
        drawerFragment.addNavItem("History", R.drawable.politician);
        drawerFragment.addNavItem("Policy Terms", R.drawable.help);
        drawerFragment.addNavItem("About us", R.drawable.about);
        drawerFragment.addNavItem("Contact", R.drawable.contact);
        drawerFragment.addNavItem("Help", R.drawable.help);
        drawerFragment.addNavItem("Logout", R.drawable.logout);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = "Home";
                break;
            case 1:
                Intent commodityIntent = new Intent(MainActivity.this, CommodityActivity.class);
                commodityIntent.putExtra("category_id",1);
                commodityIntent.putExtra("category_title","Commodity");
                startActivity(commodityIntent);
                break;

            case 2:
                Intent currencyIntent = new Intent(MainActivity.this, CommodityActivity.class);
                currencyIntent.putExtra("category_id",2);
                currencyIntent.putExtra("category_title","Currency");
                startActivity(currencyIntent);
                break;

            case 3:
                Intent rashiphalIntent = new Intent(MainActivity.this, RashiPhalActivity.class);
                startActivity(rashiphalIntent);
                break;

            case 4:
                Intent problemSolutionIntent = new Intent(MainActivity.this, ProblemActivity.class);
                startActivity(problemSolutionIntent);
                break;

            case 5:
                Intent predictionIntent = new Intent(MainActivity.this, PredictionActivity.class);
                startActivity(predictionIntent);
                break;

            case 6:
                Intent luckImprovementIntent = new Intent(MainActivity.this, LuckImprovementActivity.class);
                startActivity(luckImprovementIntent);
                break;

            case 7:
                Intent numerologyIntent = new Intent(MainActivity.this, NumerologyActivity.class);
                startActivity(numerologyIntent);
                break;

            case 8:
                Intent vastuIntent = new Intent(MainActivity.this, VastuActivity.class);
                startActivity(vastuIntent);
                break;

            case 9:
                Intent historyIntent = new Intent(MainActivity.this, HistoryCategoryActivity.class);
                startActivity(historyIntent);
                break;
            case 13:
                ApplicationStatus applicationStatus = new ApplicationStatus(this);
                applicationStatus.setEmail("");
                applicationStatus.setLoginStatus(false);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            default:
                break;
        }
        if(fragment != null){
            String backStateName =  fragment.getClass().getName();
            String fragmentTag = backStateName;

            FragmentManager fragmentManager = getSupportFragmentManager();
            boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container_body, fragment, fragmentTag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        	 /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();*/

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed(){
        System.exit(0);
    }
}

