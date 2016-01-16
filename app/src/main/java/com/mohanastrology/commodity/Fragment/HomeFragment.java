package com.mohanastrology.commodity.Fragment;

/**
 * Created by Vijay on 12/19/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mohanastrology.commodity.R;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageButton cricket, football, tennis, election;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container,
                false);

        cricket = (ImageButton) rootView.findViewById(R.id.cricket);
        tennis = (ImageButton) rootView.findViewById(R.id.tennis);
        football = (ImageButton) rootView.findViewById(R.id.football);
        election = (ImageButton) rootView.findViewById(R.id.election);

        cricket.setOnClickListener(this);
        football.setOnClickListener(this);
        tennis.setOnClickListener(this);
        election.setOnClickListener(this);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cricket:
               /* Intent i = new Intent(getActivity(), CricketActivity.class);
                getActivity().startActivity(i);*/
                break;
            case R.id.football:

                break;
            case R.id.tennis:

                break;
            case R.id.election:

                break;

            default:
                break;
        }
    }


}
