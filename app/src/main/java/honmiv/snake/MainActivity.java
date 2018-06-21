package honmiv.snake;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public void FullScreenMode() {
        //fullscreen on 4.4+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        } else
            //fullscreen on 4.1-4.3 devices
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        FullScreenMode();
        if (gamePaused) {
            gamePaused = false;
            new Handler().postDelayed(gameCycle, 500);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        gamePaused = true;
        super.onPause();
    }

    int S;
    Snake snake;
    Meal meal;
    Runnable gameCycle;

    TextView gameTView;
    TextView bckgGameTView;
    TextView pointsTView;

    Button startBtn;

    Button leftBtn;
    Button rightBtn;
    ImageView leftBtnImg;
    ImageView rightBtnImg;


    Button upBtn;
    Button downBtn;
    ImageView upBtnImg;
    ImageView downBtnImg;

    boolean gameOver;
    boolean gamePaused;
    int highScore;
    private static final String KEY_HIGHSCORE = "HIGHSCORE";
    SharedPreferences savedData;
    SharedPreferences.Editor savedDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FullScreenMode();

        savedData = getPreferences(MODE_PRIVATE);
        savedDataEditor = savedData.edit();

        startBtn = (Button) findViewById(R.id.startBtn);

        gameTView = (TextView) findViewById(R.id.gameTView);
        bckgGameTView = (TextView) findViewById(R.id.bckgGameTView);
        pointsTView = (TextView) findViewById(R.id.pointsTView);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "snake.otf");
        Typeface typeFace2 = Typeface.createFromAsset(getAssets(), "PTM55F.ttf");
        gameTView.setTypeface(typeFace);
        bckgGameTView.setTypeface(typeFace);

        pointsTView.setTypeface(typeFace2, Typeface.BOLD);
        startBtn.setTypeface(typeFace2, Typeface.BOLD);

        leftBtnImg = (ImageView) findViewById(R.id.leftBtnImg);
        leftBtn = (Button) findViewById(R.id.leftBtn);
        leftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (snake.PrevDir != 3) {
                        snake.Dir = 2;
                    }
                return false;
            }
        });

        rightBtnImg = (ImageView) findViewById(R.id.rightBtnImg);
        rightBtn = (Button) findViewById(R.id.rightBtn);
        rightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (snake.PrevDir != 2) {
                        snake.Dir = 3;
                    }
                return false;
            }
        });

        upBtnImg = (ImageView) findViewById(R.id.upBtnImg);
        upBtn = (Button) findViewById(R.id.upBtn);
        upBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (snake.PrevDir != 1) {
                        snake.Dir = 0;
                    }
                return false;
            }
        });

        downBtnImg = (ImageView) findViewById(R.id.downBtnImg);
        downBtn = (Button) findViewById(R.id.downBtn);
        downBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (snake.PrevDir != 0) {
                        snake.Dir = 1;
                    }
                return false;
            }
        });

        gameCycle = new Runnable() {
            @Override
            public void run() {
                long dt = System.currentTimeMillis();
                if (snake.Length - 4 > highScore) {
                    highScore = snake.Length - 4;
                    savedDataEditor.putInt(KEY_HIGHSCORE, highScore);
                    savedDataEditor.apply();
                    savedDataEditor.commit();
                }
                if (meal.needNewCoords)
                    meal.newCoords(snake, S);
                if (snake.Move(meal, S) != 0) {
                    showGame();
                    if (!gamePaused)
                        new Handler().postDelayed(gameCycle, 200 - (System.currentTimeMillis() - dt));
                } else {
                    leftBtn.setVisibility(View.INVISIBLE);
                    leftBtnImg.setVisibility(View.INVISIBLE);
                    rightBtn.setVisibility(View.INVISIBLE);
                    rightBtnImg.setVisibility(View.INVISIBLE);
                    upBtn.setVisibility(View.INVISIBLE);
                    upBtnImg.setVisibility(View.INVISIBLE);
                    downBtn.setVisibility(View.INVISIBLE);
                    downBtnImg.setVisibility(View.INVISIBLE);
                    startBtn.setVisibility(View.VISIBLE);
                    gameOver = true;
                    showGame();
                }
            }
        };
    }

    public void showGame() {
        String str = "";
        String backStr = "";
        for (int i = 0; i < Math.sqrt(S); i++) {
            for (int j = 0; j < Math.sqrt(S); j++) {
                if (meal.Coords.x == j && meal.Coords.y == i && !meal.needNewCoords) {
                    str += "A"; // EDA
                    backStr += "C";
                } else {
                    int k;
                    for (k = 0; k < snake.Length; k++) {
                        if (snake.Coords[k].x == j && snake.Coords[k].y == i) {
                            str += "B"; // ZMEIKA
                            backStr += " ";
                            break;
                        }
                        if (k == snake.Length - 1) {
                            backStr += "C"; // SPACE
                            str += " ";
                        }
                    }
                }
            }
            str += "\n";
            backStr += "\n";
        }
        pointsTView.setText(getString(R.string.points) + ": " + String.valueOf(snake.Length - 4));
        gameTView.setText(str);
        bckgGameTView.setText(backStr);
        if (gameOver) {
            gameTView.setText("");
            bckgGameTView.setText(str);
            pointsTView.setText(getString(R.string.game_over) + ": " + String.valueOf(snake.Length - 4) + "\n\n" + getString(R.string.high_score) + ": " + String.valueOf(highScore));
        }
    }

    public void startBtnClicked(View view) {
        if (savedData.contains(KEY_HIGHSCORE))
            highScore = savedData.getInt(KEY_HIGHSCORE, 0);
        else highScore = 0;

        leftBtn.setVisibility(View.VISIBLE);
        leftBtnImg.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtnImg.setVisibility(View.VISIBLE);
        upBtn.setVisibility(View.VISIBLE);
        upBtnImg.setVisibility(View.VISIBLE);
        downBtn.setVisibility(View.VISIBLE);
        downBtnImg.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.INVISIBLE);

        gameOver = false;
        gamePaused = false;

        S = 20 * 20;

        snake = new Snake(S);
        meal = new Meal();
        meal.newCoords(snake, S);

        showGame();
        new Handler().postDelayed(gameCycle, 500);
    }
}
