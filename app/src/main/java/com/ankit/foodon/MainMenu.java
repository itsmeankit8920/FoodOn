package com.ankit.foodon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
Button signinemail,signinphone,signup;
ImageView bgimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        final Animation zoomin= AnimationUtils.loadAnimation(this,R.anim.zoomin);
        final Animation zoomout= AnimationUtils.loadAnimation(this, R.anim.zoomout);
        bgimage=findViewById(R.id.back2);
        bgimage.setAnimation(zoomin);
        bgimage.setAnimation(zoomout);
        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               bgimage.startAnimation(zoomin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bgimage.startAnimation(zoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
signinemail=(Button)findViewById(R.id.SignwithEmail);
signinphone=(Button)findViewById(R.id.SignwithPhone);
signup=(Button)findViewById(R.id.Signup);

signinemail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent signemail=new Intent(MainMenu.this,ChooseOne.class);
        signemail.putExtra("Home","Email");
        startActivity(signemail);
        finish();

    }
});
        signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signemail=new Intent(MainMenu.this,ChooseOne.class);
                signemail.putExtra("Home","Phone");
                startActivity(signemail);
                finish();

            }
        });
signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent signemail=new Intent(MainMenu.this,ChooseOne.class);
        signemail.putExtra("Home","SignUp");
        startActivity(signemail);
        finish();

    }
});

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.gc();
    }
}