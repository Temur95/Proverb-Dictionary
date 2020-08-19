package com.temur.myword.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.temur.myword.R;
import com.temur.myword.di.ApplicationContext;
import com.temur.myword.model.ProverbList;
import com.temur.myword.ui.callback.ProverbAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ProverbAdapter.OnProverbClickListener {

    private static final String TAG = "MainActivity";

    private ProverbList proverbList;
    private ProverbList maqolList;

    ProverbAdapter adapter;

    Gson gson = new Gson();
    Gson gsonUz = new Gson();

    private boolean doubleBackToExitPressedOnce = false;
    private boolean uz = false;
    private boolean layoutOpened = false;

    @Inject
    @ApplicationContext
    Context context;

    @BindView(R.id.edit_search)
    EditText edit_search;
    @BindView(R.id.list_proverb)
    RecyclerView list_proverb;
    @BindView(R.id.layout_list)
    ConstraintLayout layout_list;
    @BindView(R.id.layout_search)
    ConstraintLayout layout_search;
    @BindView(R.id.text_proverb_long)
    TextView text_proverb;
    @BindView(R.id.text_explanation_long)
    TextView text_explanation;
    @BindView(R.id.text_eqvuivalent_long)
    TextView text_equivalent;
    @BindView(R.id.text_footnote_long)
    TextView text_footnote;
    @BindView(R.id.button_close_info)
    Button button_close_info;
    @BindView(R.id.layout_proverb_info)
    ConstraintLayout layout_proverb_info;
    @BindView(R.id.toggle_language)
    ToggleButton toggle_language;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(layoutOpened){
            animateSlideOutBottom(layout_proverb_info);
            layoutOpened = false;
        }else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Нажмите назад снова, чтобы выйти", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Dictionary of Proverbs");

        getJSon();

        setRounded(edit_search, 8);


        adapter = new ProverbAdapter(this, proverbList.getProverbList(), this);
        list_proverb.setLayoutManager(new LinearLayoutManager(context));
        list_proverb.setAdapter(adapter);

        toggle_language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.getFilter().filter("");
                if(isChecked){
                    adapter = new ProverbAdapter(MainActivity.this, maqolList.getProverbList(), MainActivity.this);
                    uz = true;
                }else {
                    adapter = new ProverbAdapter(MainActivity.this, proverbList.getProverbList(), MainActivity.this);
                    uz = false;
                }
                edit_search.setText("");
                adapter.notifyDataSetChanged();
                list_proverb.setAdapter(adapter);
            }
        });


        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               adapter.getFilter().filter(s);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        button_close_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateSlideOutBottom(layout_proverb_info);
                layoutOpened = false;
            }
        });

        layout_proverb_info.setOnClickListener(null);
    }

    public void getJSon(){
        String proverb="?";
        String maqol="?";

        try {

            InputStream is = getAssets().open("proverb.json");
            InputStream m = getAssets().open("maqol.json");
            int size = 0;
            int size2 = 0;
            size = is.available();
            size2 = m.available();
            byte[] buffer = new byte[size];
            byte[] buffer2 = new byte[size2];

            is.read(buffer);
            m.read(buffer2);

            is.close();
            m.close();

            proverb = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(proverb);

            maqol = new String(buffer2, "UTF-8");
            JSONObject obj2 = new JSONObject(maqol);

        } catch (IOException e) {
            e.printStackTrace();
        }   catch (JSONException e){
            e.printStackTrace();
        }

        proverbList = gson.fromJson(proverb, ProverbList.class);
        maqolList = gsonUz.fromJson(maqol, ProverbList.class);

        Log.d(TAG, "getResult: "+maqolList.getProverbList().get(0).getProverb());
        //Toast.makeText(this, maqolList.getProverbList().get(0).getProverb(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProverbClick(int position) {
        closeKeyBoard();
        if(layout_proverb_info.getVisibility()==View.GONE)
            layout_proverb_info.setVisibility(View.VISIBLE);

        ProverbList list;
        if(uz){
            list = maqolList;
        }else {
            list = proverbList;
        }
        text_proverb.setText(list.getProverbList().get(position).getProverb());
        text_explanation.setText(list.getProverbList().get(position).getExplanation());
        if(list.getProverbList().get(position).getEquivalent().equals(""))
            text_equivalent.setText("-");
        else
            text_equivalent.setText(list.getProverbList().get(position).getEquivalent());

        if(list.getProverbList().get(position).getFootnote()==null)
            text_footnote.setText("-");
        else
            text_footnote.setText(list.getProverbList().get(position).getFootnote());
        animateSlideInBottom(layout_proverb_info);
        layoutOpened = true;
    }

    public void setRounded(View view, int dp){
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int color = Color.parseColor("#FAFAFA");

        int pixels = (int) (dp * scale);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(pixels);

        view.setBackground(gd);
    }

    public void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public void animateSlideInBottom(View slide){
        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .playOn(slide);
    }

    public void animateSlideOutBottom(View slide){
        YoYo.with(Techniques.SlideOutDown)
                .duration(500)
                .playOn(slide);
    }
}
