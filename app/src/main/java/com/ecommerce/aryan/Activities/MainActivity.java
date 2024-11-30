package com.ecommerce.aryan.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.ecommerce.aryan.Fragments.CartFragment;
import com.ecommerce.aryan.Fragments.FavouriteFragment;
import com.ecommerce.aryan.Fragments.HomeFragment;
import com.ecommerce.aryan.Fragments.ProfileFragment;
import com.ecommerce.aryan.Fragments.SearchFragment;
import com.ecommerce.aryan.Helpers.CartManager;
import com.ecommerce.aryan.Models.CartModel;
import com.ecommerce.aryan.R;
import com.ecommerce.aryan.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Fragment fragment;
    ObservableArrayList<CartModel> cartItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartItems = CartManager.loadCartItems(MainActivity.this);
        cartItems.addOnListChangedCallback(new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                binding.bottomNav.setCount(3, String.valueOf(cartItems.size()));
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                binding.bottomNav.setCount(3, String.valueOf(cartItems.size()));
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                binding.bottomNav.setCount(3, String.valueOf(cartItems.size()));
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                binding.bottomNav.setCount(3, String.valueOf(cartItems.size()));
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                binding.bottomNav.setCount(3, String.valueOf(cartItems.size()));
            }
        });

        binding.bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        binding.bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_search));
        binding.bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_cart));
        binding.bottomNav.add(new MeowBottomNavigation.Model(4, R.drawable.ic_heart));
        binding.bottomNav.add(new MeowBottomNavigation.Model(5, R.drawable.ic_user));
        Bundle bundle = new Bundle();
        bundle.putSerializable("cartItems", cartItems);

        Fragment f1 = new HomeFragment();
        f1.setArguments(bundle);
        Fragment f2 = new SearchFragment();
        Fragment f3 = new CartFragment();
        f3.setArguments(bundle);
        Fragment f4 = new FavouriteFragment();
        Fragment f5 = new ProfileFragment();


        binding.bottomNav.setCount(3, String.valueOf(cartItems.size()));


        binding.bottomNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {

                    case 1:
                        fragment = f1;
                        break;
                    case 2:
                        fragment = f2;
                        break;
                    case 4:
                        fragment = f4;
                        break;
                    case 5:
                        fragment = f5;
                        break;
                    default:
                        fragment = f3;
                        break;

                }

                loadFragment(fragment);
            }
        });

        binding.bottomNav.show(1, true);

        binding.bottomNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(MainActivity.this, String.valueOf(item.getId()), Toast.LENGTH_SHORT).show();



            }
        });


        binding.bottomNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        CartManager.saveCartItems(MainActivity.this,cartItems);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fc, fragment, null)
                .commit();
    }
}