package com.tu.place.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.adapter.AdapterComment;
import com.tu.place.firebase.FirebaseManager;
import com.tu.place.model.ArrayList;
import com.tu.place.model.Comment;
import com.tu.place.model.Place;
import com.tu.place.model.User;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppDialogManager;
import com.tu.place.utils.AppUtils;

import java.util.Calendar;


public class PlaceDetailFragment extends Fragment implements View.OnClickListener, AdapterComment.ItemListener{

    private Place place;
    private FirebaseManager firebaseManager;

    public PlaceDetailFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PlaceDetailFragment(Place place) {
        this.place = place;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    ImageView imPlace;
    TextView tvContent, tvAddess, tvTitle, tvDistance;
    RatingBar ratingBar;
    ImageButton btnDirection;
    RecyclerView rvComment;
    TextView tvNoComment;
    Button btnAddComment;
    MainActivity mainActivity;
    ArrayList<Comment> listComment;
    AdapterComment mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_detail, container, false);
        imPlace = (ImageView) rootView.findViewById(R.id.imPlace);
        tvContent = (TextView) rootView.findViewById(R.id.tvContent);
        tvAddess = (TextView) rootView.findViewById(R.id.tvAddress);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvDistance = (TextView) rootView.findViewById(R.id.tvDistance);
        ratingBar = (RatingBar) rootView.findViewById(R.id.rating);
        btnDirection = (ImageButton) rootView.findViewById(R.id.ibtnDirection);
        rvComment = (RecyclerView) rootView.findViewById(R.id.rvComment);
        btnAddComment = (Button) rootView.findViewById(R.id.btnAddComment);
        tvNoComment = (TextView) rootView.findViewById(R.id.tv_no_comment);
        mainActivity = (MainActivity) getActivity();
        btnDirection.setOnClickListener(this);
        btnAddComment.setOnClickListener(this);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);
        firebaseManager = new FirebaseManager();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvComment.setLayoutManager(mLayoutManager);
        listComment = new ArrayList<>();
        mAdapter = new AdapterComment(mainActivity, listComment, this);
        rvComment.setAdapter(mAdapter);
        setData();
        listenCommentChange();
        return rootView;
    }

    private void listenCommentChange(){

        if (AppUtils.isNetworkAvailable(mainActivity)) {
            mainActivity.listPlace.clear();
            Log.d(AppContants.TAG, place.getId());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child(AppContants.FIREBASE_COMMENT_TABLE)
                    .orderByChild(AppContants.FIREBASE_PLACE_ID_COL)
                    .equalTo(place.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Log.d(AppContants.TAG, dataSnapshot.toString());
                                listComment.clear();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    final Comment comment = child.getValue(Comment.class);
                                    firebaseManager.getRef(AppContants.FIREBASE_USER_TABLE + "/" + comment.getUserId(), new FirebaseManager.Callback() {
                                        @Override
                                        public void success(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                Log.d(AppContants.TAG, dataSnapshot.toString());
                                                User user = dataSnapshot.getValue(User.class);
                                                comment.setUserName(user.name);
                                                comment.setUrlAvatar(user.url);
                                                listComment.add(comment);
                                            }else  Log.d(AppContants.TAG, "user null");
                                            if(listComment.size()>0) {
                                                rvComment.setVisibility(View.VISIBLE);
                                                tvNoComment.setVisibility(View.GONE);
                                                mAdapter.notifyDataSetChanged();
                                            }else {
                                                rvComment.setVisibility(View.GONE);
                                                tvNoComment.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void failed() {
//                                            listComment.add(comment);
                                            Snackbar.make(btnAddComment, "Đã có lỗi xảy ra, vui lòng thử lại", Snackbar.LENGTH_LONG).show();
                                        }
                                    });

                                }

                            }else  Log.d(AppContants.TAG, "data null");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(AppContants.TAG, databaseError.toString());
                            Snackbar.make(btnAddComment, "Đã có lỗi xảy ra, vui lòng thử lại", Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else Snackbar.make(btnAddComment, "Vui lòng kiểm tra lại kết nối", Snackbar.LENGTH_LONG).show();
    }

    private void setData(){
        tvTitle.setText(place.getTitle());
        tvAddess.setText(place.getAddress());
        tvContent.setText(place.getContent());
        tvDistance.setText(String.valueOf(place.getDistance()/1000f)+" km");
        ratingBar.setRating(Float.parseFloat(String.valueOf(place.getScore())));
        if(!AppUtils.isEmptyString(place.getImg())) {
            Picasso.with(getActivity()).load(AppContants.getURLImg(place.getImg())).placeholder(R.drawable.ic_map).into(imPlace);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lnControl.setVisibility(View.GONE);
    }

    private void showAddComment(){
        AppDialogManager.onShowAddCommentDialog(getActivity(), new AppDialogManager.DialogClickListener() {
            @Override
            public void onAccept(View view) {
                Comment comment = new Comment();
                comment.setContent(view.getTag().toString());
                comment.setUserId(MainActivity.user.username);
                comment.setPlaceId(place.getId());
                comment.setTime(Calendar.getInstance().getTimeInMillis());
                if (AppUtils.isNetworkAvailable(mainActivity)) {
                    firebaseManager.writeRef(AppContants.FIREBASE_COMMENT_TABLE, AppUtils.getRandomIdCmt(MainActivity.user.username, place.getId()), comment, new FirebaseManager.Callback() {
                        @Override
                        public void success(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void failed() {
                            Snackbar.make(btnAddComment, "Đã có lỗi xảy ra, vui lòng thử lại", Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else Toast.makeText(mainActivity, "Vui lòng kiểm tra lại kết nối", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(View view) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAddComment:
                showAddComment();
                break;
            case R.id.ibtnDirection:
                getActivity().getFragmentManager().popBackStack();
                ((MainActivity)getActivity()).showDirection(place);
                break;
            case R.id.rating:

                break;
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }
}
