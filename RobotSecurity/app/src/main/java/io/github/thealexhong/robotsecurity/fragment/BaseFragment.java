package io.github.thealexhong.robotsecurity.fragment;

import android.app.Fragment;
import android.widget.Toast;

import io.github.thealexhong.robotsecurity.R;

/**
 * Base class for other fragments
 */
public class BaseFragment extends Fragment {
    /**
     * Switch fragments
     * @param frag    switch current fragment with frag
     */
    protected void swapFragment(Fragment frag) {
        getActivity().getFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, frag)
                .addToBackStack(null).commit();
    }

    /**
     * Switch to previous fragment
     */
    protected void getPrevFragment () {
        getFragmentManager().popBackStack();
    }

    /**
     * Shows message box inside the fragment
     * @param msg
     */
    public void showNotification (String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

