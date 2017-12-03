package com.example.englandhoang.munichats;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EnglandHoang on 12/02/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        /*Left side*/
        public CircleImageView profileImage;
        public ImageView messageImage;
        public TextView messageText, messageName, messageTime, messageNameImage, messageTimeImage;

        /*Right side*/
        public ImageView messageImageRight;
        public TextView messageTextRight, messageNameRight, messageTimeRight, messageNameImageRight, messageTimeImageRight;

        public MessageViewHolder(View itemView) {
            super(itemView);

            /*Left side*/
            profileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_layout);
            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            messageImage = (ImageView) itemView.findViewById(R.id.message_image_layout);
            messageName = (TextView) itemView.findViewById(R.id.name_text_layout);
            messageTime = (TextView) itemView.findViewById(R.id.time_text_layout);
            messageNameImage = (TextView) itemView.findViewById(R.id.name_text_layout_image);
            messageTimeImage = (TextView) itemView.findViewById(R.id.time_text_layout_image);

            /*Right side*/
            messageTextRight = (TextView) itemView.findViewById(R.id.message_text_layout_right);
            messageImageRight = (ImageView) itemView.findViewById(R.id.message_image_layout_right);
            messageNameRight = (TextView) itemView.findViewById(R.id.name_text_layout_right);
            messageTimeRight = (TextView) itemView.findViewById(R.id.time_text_layout_right);
            messageNameImageRight = (TextView) itemView.findViewById(R.id.name_text_layout_image_right);
            messageTimeImageRight = (TextView) itemView.findViewById(R.id.time_text_layout_image_right);


        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();

        String current_user_id = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(position);

        String from_user = c.getFrom();
        String message_type = c.getType();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                holder.messageName.setText(name);
                holder.messageNameImage.setText(name);

                holder.messageNameRight.setText(name);
                holder.messageNameImageRight.setText(name);

                Picasso.with(holder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.default_avatar).into(holder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (current_user_id.equals(from_user)) {
            holder.profileImage.setVisibility(View.GONE);
            holder.messageImage.setVisibility(View.GONE);
            holder.messageText.setVisibility(View.GONE);
            holder.messageName.setVisibility(View.GONE);
            holder.messageTime.setVisibility(View.GONE);
            holder.messageNameImage.setVisibility(View.GONE);
            holder.messageTimeImage.setVisibility(View.GONE);

            holder.messageImageRight.setVisibility(View.VISIBLE);
            holder.messageTextRight.setVisibility(View.VISIBLE);
            holder.messageNameRight.setVisibility(View.VISIBLE);
            holder.messageTimeRight.setVisibility(View.VISIBLE);
            holder.messageNameImageRight.setVisibility(View.VISIBLE);
            holder.messageTimeImageRight.setVisibility(View.VISIBLE);

            if (message_type.equals("text")) {

                holder.messageTextRight.setText(c.getMessage());
                holder.messageImageRight.setVisibility(View.GONE);
                holder.messageTextRight.setVisibility(View.VISIBLE);
                holder.messageNameImageRight.setVisibility(View.INVISIBLE);
                holder.messageTimeImageRight.setVisibility(View.INVISIBLE);
                holder.messageNameRight.setVisibility(View.VISIBLE);
                holder.messageTimeRight.setVisibility(View.VISIBLE);

            } else {

                holder.messageImageRight.setVisibility(View.VISIBLE);
                holder.messageTextRight.setVisibility(View.GONE);
                Picasso.with(holder.profileImage.getContext()).load(c.getMessage())
                        .placeholder(R.drawable.default_avatar).into(holder.messageImageRight);
                holder.messageNameRight.setVisibility(View.INVISIBLE);
                holder.messageTimeRight.setVisibility(View.INVISIBLE);
                holder.messageNameImageRight.setVisibility(View.VISIBLE);
                holder.messageTimeImageRight.setVisibility(View.VISIBLE);

            }


            if (from_user.equals(current_user_id)) {

                holder.messageTextRight.setBackgroundResource(R.drawable.message_text_background);
                holder.messageImageRight.setBackgroundResource(R.drawable.message_text_background);
                holder.messageTextRight.setTextColor(Color.WHITE);


            } else {

                holder.messageTextRight.setBackgroundResource(R.drawable.message_text_background2);
                holder.messageImageRight.setBackgroundResource(R.drawable.message_text_background2);
                holder.messageTextRight.setTextColor(Color.BLACK);

            }

            holder.messageTextRight.setText(c.getMessage());
            holder.messageTimeRight.setText(DateFormat.getTimeInstance().format(c.getTime()));
            holder.messageTimeImageRight.setText(DateFormat.getTimeInstance().format(c.getTime()));

        } else {
            holder.profileImage.setVisibility(View.VISIBLE);
            holder.messageImage.setVisibility(View.VISIBLE);
            holder.messageText.setVisibility(View.VISIBLE);
            holder.messageName.setVisibility(View.VISIBLE);
            holder.messageTime.setVisibility(View.VISIBLE);
            holder.messageNameImage.setVisibility(View.VISIBLE);
            holder.messageTimeImage.setVisibility(View.VISIBLE);

            holder.messageImageRight.setVisibility(View.GONE);
            holder.messageTextRight.setVisibility(View.GONE);
            holder.messageNameRight.setVisibility(View.GONE);
            holder.messageTimeRight.setVisibility(View.GONE);
            holder.messageNameImageRight.setVisibility(View.GONE);
            holder.messageTimeImageRight.setVisibility(View.GONE);

            if (message_type.equals("text")) {

                holder.messageText.setText(c.getMessage());
                holder.messageImage.setVisibility(View.GONE);
                holder.messageText.setVisibility(View.VISIBLE);
                holder.messageNameImage.setVisibility(View.INVISIBLE);
                holder.messageTimeImage.setVisibility(View.INVISIBLE);
                holder.messageName.setVisibility(View.VISIBLE);
                holder.messageTime.setVisibility(View.VISIBLE);

            } else {

                holder.messageImage.setVisibility(View.VISIBLE);
                holder.messageText.setVisibility(View.GONE);
                Picasso.with(holder.profileImage.getContext()).load(c.getMessage())
                        .placeholder(R.drawable.default_avatar).into(holder.messageImage);
                holder.messageName.setVisibility(View.INVISIBLE);
                holder.messageTime.setVisibility(View.INVISIBLE);
                holder.messageNameImage.setVisibility(View.VISIBLE);
                holder.messageTimeImage.setVisibility(View.VISIBLE);

            }


            if (from_user.equals(current_user_id)) {

                holder.messageText.setBackgroundResource(R.drawable.message_text_background);
                holder.messageImage.setBackgroundResource(R.drawable.message_text_background);
                holder.messageText.setTextColor(Color.WHITE);


            } else {

                holder.messageText.setBackgroundResource(R.drawable.message_text_background2);
                holder.messageImage.setBackgroundResource(R.drawable.message_text_background2);
                holder.messageText.setTextColor(Color.BLACK);

            }


            holder.messageText.setText(c.getMessage());
            holder.messageTime.setText(DateFormat.getTimeInstance().format(c.getTime()));
            holder.messageTimeImage.setText(DateFormat.getTimeInstance().format(c.getTime()));
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
