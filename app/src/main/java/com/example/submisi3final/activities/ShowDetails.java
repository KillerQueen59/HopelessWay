package com.example.submisi3final.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.submisi3final.R;
import com.example.submisi3final.model.Content;
import com.squareup.picasso.Picasso;

import com.example.submisi3final.db.DatabaseContract;
import com.example.submisi3final.db.DatabaseHelper;
import com.example.submisi3final.db.NoteHelper;

public class ShowDetails extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUES_UPDATE = 400;
    public static final int RESULT_DELETE = 300;

    private Content content;
    private int position;

    private NoteHelper noteHelper;
    private SQLiteDatabase db;

    String titleMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        final Content content = getIntent().getParcelableExtra(EXTRA_TITLE);
        noteHelper = new NoteHelper(this);

        ImageView Poster = findViewById(R.id.posterBesarMovie);
        TextView Title = findViewById(R.id.titleDetailsMovie);
        TextView Desc = findViewById(R.id.descMovie);
        TextView Date = findViewById(R.id.dateMovie);
        TextView Rate = findViewById(R.id.rateText);
        ImageButton Fav = findViewById(R.id.btnMoreFav);
        ImageButton UnFav = findViewById(R.id.btnUnFav);


        Rate.setText(content.getRateContet() + "/10");
        titleMovie = content.getTitleContent();
        Title.setText(titleMovie);
        Desc.setText(content.getDescContent());
        Date.setText(content.getDateContent());
        Picasso.with(this).load(content.getPosterContent()).into(Poster);
        if (sudahAda(titleMovie)) {
            Fav.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           long result = noteHelper.insertNote(content);
                                           if (result > 0) {
                                               content.setId((int) result);
                                               setResult(RESULT_ADD);
                                           }
                                           finish();
                                           Toast.makeText(ShowDetails.this, "Data ditambahkan", Toast.LENGTH_SHORT).show();
                                       }
                                   }
            );
            UnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long result = noteHelper.deleteNote(content.getId());
                    if (result > 0) {
                        Intent intent = new Intent(ShowDetails.this, MainActivity.class);
                        intent.putExtra(EXTRA_POSITION, position);
                        setResult(RESULT_DELETE, intent);
                        startActivity(intent);
                    }
                    Toast.makeText(ShowDetails.this, "Terhapus", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public boolean sudahAda(String itemDicari){
        String[] projection = {
                DatabaseContract.NoteColumns._ID,
                DatabaseContract.NoteColumns.TITLE,
                DatabaseContract.NoteColumns.DATE,
                DatabaseContract.NoteColumns.DESC,
                DatabaseContract.NoteColumns.RATE,
                DatabaseContract.NoteColumns.POSTER
        };
        String seleksi = DatabaseContract.NoteColumns.TITLE + " =?";
        String[] seleksiArgs = {itemDicari};
        String batas = "1";

        Cursor cursor = db.query(DatabaseContract.NoteColumns.TABLE_NAME,projection,seleksi,seleksiArgs,null,null,null,batas);
        boolean sudah = (cursor.getCount() > 0 );
        cursor.close();
        return sudah;
    }
}