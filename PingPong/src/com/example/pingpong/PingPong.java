package com.example.pingpong;

import java.util.Vector;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class PingPong extends Activity implements OnClickListener{

	private int score1, score2;
    private int PLAYER_1 = 0;
	private int PLAYER_2 = 1;
	private int DEFAULT_GAME_POINT =15;
	private final String GAME_POINT_KEY = "Game_Point";
	private int gamePoint = 15;
	private TextView gameInfoText;
	private TextView scoreTxt1, scoreTxt2;
	private Button scoreButton1, scoreButton2;
	private Button gamePointButton, resetScoresButton, undoButton;
	//private int lastScore =0;
	private int currServe;
	private static final String NEW_15_POINT_GAME = "15 point game";
	private static final String NEW_21_POINT_GAME = "21 point game";
	private static final String DEUCE = "Deuce";
	private static final String ADVANTAGE_PLAYER_1 = "Advantage Player 1";
	private static final String ADVANTAGE_PLAYER_2 = "Advantage Player 2";
	private Vector<Score> scoreList;
	
	public class Score{
		private int playerIndex, servingPlayer;
		public Score(int playerId, int serverId){
			playerIndex=playerId;
			servingPlayer = serverId;
		}
		public int getPlayerId(){
			return playerIndex;
		}
		public int getServe(){
			return servingPlayer;
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreList=new Vector<Score>();
        gamePointButton = (Button)findViewById(R.id.buttonChooseGamePoint);
        gamePointButton.setOnClickListener(this);
        resetScoresButton = (Button) findViewById (R.id.buttonReset);
        resetScoresButton.setOnClickListener(this);
        gameInfoText = (TextView)findViewById(R.id.matchInfo);
        scoreTxt1 = (TextView)findViewById(R.id.score1);
        scoreTxt2 = (TextView)findViewById(R.id.score2);
        scoreButton1 = (Button)findViewById(R.id.button1);
        scoreButton1.setOnClickListener(this);
        scoreButton2 = (Button)findViewById(R.id.button2);
        scoreButton2.setOnClickListener(this);
        undoButton = (Button)findViewById(R.id.undoButton);
        undoButton.setOnClickListener(this);
        newGame();
	}

	private void setServe(int playerIndex){
		if (playerIndex==PLAYER_1){
			currServe=PLAYER_1;
			scoreTxt1.setTextColor(getResources().getColor(R.color.white));
			scoreTxt1.setBackgroundColor(getResources().getColor(R.color.black));
			scoreTxt2.setTextColor(getResources().getColor(R.color.black));
			scoreTxt2.setBackgroundColor(getResources().getColor(R.color.white));
		}else{
            currServe=PLAYER_2;
			scoreTxt2.setTextColor(getResources().getColor(R.color.white));
			scoreTxt2.setBackgroundColor(getResources().getColor(R.color.black));
			scoreTxt1.setTextColor(getResources().getColor(R.color.black));
			scoreTxt1.setBackgroundColor(getResources().getColor(R.color.white));
		}
	}
	
	public void onClick(View v) {
		int viewId = v.getId();
		switch(viewId){
		case R.id.button1:
			addScore(PLAYER_1);
			break;
		case R.id.button2:
			addScore(PLAYER_2);
			break;
		case R.id.buttonChooseGamePoint:
			chooseGamePoint();
			break;
		case R.id.buttonReset:
			newGame();
			break;
		case R.id.undoButton:
			undoScore();
			break;
		default:
			break;
		}
		
	}


	private void undoScore() {
		// TODO Auto-generated method stub
		if (scoreList.size()==0){
			return;
		}
		if (scoreList.get(scoreList.size()-1).getPlayerId() == PLAYER_1){
			if (score1 > 0){
				score1--;
			}
			checkSpecial(false);
			scoreTxt1.setText(""+score1);
			scoreTxt1.invalidate();
		}
		if (scoreList.get(scoreList.size()-1).getPlayerId() == PLAYER_2){
			if (score2 > 0){
				score2--;
			}
			checkSpecial(false);
			scoreTxt2.setText(""+score2);
			scoreTxt1.invalidate();
		}
		scoreList.remove(scoreList.size()-1);
		if (scoreList.size()>0){
			setServe(scoreList.get(scoreList.size()-1).getServe());
		}
		
		
	}

	private void updateGameInfo(){
		if (gamePoint==DEFAULT_GAME_POINT){
			gameInfoText.setText(NEW_15_POINT_GAME);
		}else{
			gameInfoText.setText(NEW_21_POINT_GAME);
		}
		checkSpecial(true);
	}
	
	private void newGame() {
		scoreList.clear();
		currServe=0;
		score1=0;
		score2=0;
		if (gamePoint==DEFAULT_GAME_POINT){
			gameInfoText.setText(NEW_15_POINT_GAME);
		}else{
			gameInfoText.setText(NEW_21_POINT_GAME);
		}
		scoreTxt1.setText(""+score1);
		scoreTxt1.invalidate();
		scoreTxt2.setText(""+score2);
		scoreTxt2.invalidate();
		final CharSequence[] items = {"Player 1 Won","Player 2 Won"};
        AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(this);
        lAlertDialog.setTitle("Volley For Serve");
        lAlertDialog.setCancelable(true);
        lAlertDialog.setItems(items,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i){
                if (i == 0){
                	setServe(PLAYER_1);
                }
                if (i == 1){
                    setServe(PLAYER_2);
                }
                updateGameInfo();
            }
        });
        lAlertDialog.show();
	}

	private void chooseGamePoint() {
		// TODO Auto-generated method stub
		final CharSequence[] items = {"Play to 15","Play to 21"};
        AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(this);
        lAlertDialog.setTitle("Choose Game Point");
        lAlertDialog.setCancelable(true);
        lAlertDialog.setItems(items,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i){
                if (i == 0){
                    gamePoint=DEFAULT_GAME_POINT;
                }
                if (i == 1){
                    gamePoint=21;
                }
                updateGameInfo();
            }
        });
        lAlertDialog.show();
	    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putInt(GAME_POINT_KEY, DEFAULT_GAME_POINT);
	    editor.commit();

	}

	private void addScore(int playerIndex) {
		// TODO Auto-generated method stub
		if (playerIndex == PLAYER_1){
			score1++;
			checkSpecial(true);
			scoreTxt1.setText(""+score1);
			scoreTxt1.invalidate();
			scoreList.add(new Score(PLAYER_1, currServe));
		}
		if (playerIndex == PLAYER_2){
			score2++;
			checkSpecial(true);
			scoreTxt2.setText(""+score2);
			scoreTxt1.invalidate();
			scoreList.add(new Score(PLAYER_2, currServe));
		}
	}

	private void checkSpecial(boolean adding) {
		if ((score1<gamePoint-1)&&(score2<gamePoint-1)){
			checkForServes(adding);
			return;
		}
		if ((score1==gamePoint-1)&&(score2<gamePoint-1)){
			//player1 gamepoint
			gameInfoText.setText("Game Point Player 1");
			setServe(PLAYER_2);
			return;
		}
		if ((score2==gamePoint-1)&&(score1<gamePoint-1)){
			//player2 gamepoint
			gameInfoText.setText("Game Point Player 2");
			setServe(PLAYER_1);
			return;
		}
		if (score1>=score2){
			if (score1>=gamePoint){
				if (score1>score2+1){
					//win p1
					gameInfoText.setText("Player 1 Wins");
					return;
				}else if (score1==score2){
					//deuce
					gameInfoText.setText(DEUCE);
					if (scoreList.get(scoreList.size()-1).getPlayerId()==PLAYER_1){
						setServe(PLAYER_2);
					}else{
						setServe(PLAYER_1);
					}
					return;
				}else if (score2>=gamePoint-1){
					//advantage1
					gameInfoText.setText(ADVANTAGE_PLAYER_1);
					setServe(PLAYER_2);
					return;
				}
			}
		}else{
			if (score2>=gamePoint){
				if (score2>score1+1){
					//win p2
					gameInfoText.setText("Player 2 Wins");
					return;
				}else if (score1>=gamePoint-1){
					//advantage2
					gameInfoText.setText(ADVANTAGE_PLAYER_2);
					setServe(PLAYER_1);
					return;
				}
			}
		}
		if ((score2==gamePoint-1)&&(score1==score2)){
			gameInfoText.setText(DEUCE);
			if (scoreList.get(scoreList.size()-1).getPlayerId()==PLAYER_1){
				setServe(PLAYER_2);
			}else{
				setServe(PLAYER_1);
			}
			return;
		}
	}

	private void checkForServes(boolean adding) {
		// TODO Auto-generated method stub
		if ((score1==score2)&&score1==0){
			return;
		}
		if (adding==true){
			if ((score1+score2)%5==0){
				if (currServe==PLAYER_1){
					setServe(PLAYER_2);
				}else{
					setServe(PLAYER_1);
				}
			}
		}else{
			
		}
	}
    
}
