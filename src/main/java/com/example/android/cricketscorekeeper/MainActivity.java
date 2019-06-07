package com.example.android.cricketscorekeeper;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int scoreOfTeamA = 0; //score of team A
    int scoreOfTeamB = 0; //score of team B
    int target = 0;       //target to win game
    int teamAOvers = 0;   //overs played by team A
    int teamBOvers = 0;   //overs played by team B
    int teamABalls = 0;   //balls played by team A
    int teamBBalls = 0;   //balls played by team B
    int teamAExtras = 0;  //extra runs for team A
    int teamBExtras = 0;  //extra runs for team B
    int teamAWickets = 0; //wickets of team A
    int teamBWickets = 0; //wickets of team B
    int max_overs=5; //in this app max overs set to 5 but we can change that by just changing value of variable
    char currently_playing = 'A';

    String status;
    boolean wasnoball = false;
    char tossWon;
    boolean gameEnd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    when increment_runs is called it will check if game has been ended or not
    if game is already ended then no increment will be done, else if game is currently live
    then it will check for which team is batting,
    then check if team is chasing or batting first,
    in case of batting first if overs become max_overs then it will set target one more than total score
    and pass control of current batting to another team
    else if betting team is chasing score then it will check if max_overs reached if true then game will end
     */
    private void increment_runs(int num) {
        if (!gameEnd) { //check if game is already ended
            if (currently_playing == 'A') {
                if (++teamABalls == 6) {    //since 1 over = 6 balls
                    teamABalls = 0; //for every six balls set balls=0 and over=over+1
                    teamAOvers++;
                }
                scoreOfTeamA += num;    //increment score
                if (tossWon == 'B' && target <= scoreOfTeamA) { //if team reached score more then target then it won
                    status = "Team A won by " + (10 - teamAWickets) + " wickets";
                    gameEnd = true; //end the game as team won
                } else if (teamAOvers == max_overs) {
                    if (tossWon == 'A') {
                        currently_playing = 'B';
                        target = scoreOfTeamA + 1;
                    } else {
                        if (scoreOfTeamA < target) {
                            status = "Team A lost by  " + (target - scoreOfTeamA) + " runs";
                            gameEnd = true;
                        }
                    }
                }
            } else if (currently_playing == 'B') {
                if (++teamBBalls == 6) {
                    teamBBalls = 0;
                    teamBOvers++;
                }
                scoreOfTeamB += num;
                if (tossWon == 'A' && target <= scoreOfTeamB) {
                    status = "Team B won by " + (10 - teamBWickets) + " wickets";
                    gameEnd = true;
                } else if (teamBOvers == max_overs) {
                    if (tossWon == 'B') {
                        currently_playing = 'A';
                        target = scoreOfTeamB + 1;
                    } else {
                        if (scoreOfTeamB < target) {
                            status = "Team B lost by  " + (target - scoreOfTeamB) + " runs";
                            gameEnd = true;
                        }
                    }
                }
            }
            wasnoball = false; //since after a no ball if team scores any run then No ball period is over
        }
    }

    //This method will increment run to zero (this needed because we need to update overs)
    public void zeroRuns(View view) {
        if (!gameEnd) {
            increment_runs(0);
        }
        displaystatus();
    }

    /*All methods from here oneRuns to sixRuns pass corresponding value to increment*/
    public void oneRuns(View view) {
        if (!gameEnd) {
            increment_runs(1);
        }
        displaystatus();
    }


    public void twoRuns(View view) {
        if (!gameEnd) {
            increment_runs(2);
        }
        displaystatus();
    }

    public void threeRuns(View view) {
        if (!gameEnd) {
            increment_runs(3);
        }
        displaystatus();
    }

    public void fourRuns(View view) {
        if (!gameEnd) {
            increment_runs(4);
        }
        displaystatus();
    }

    public void fiveRuns(View view) {
        if (!gameEnd) {
            increment_runs(5);
        }
        displaystatus();
    }

    public void sixRuns(View view) {
        if (!gameEnd) {
            increment_runs(6);
        }
        displaystatus();
    }

    //In case of wide, balls will not  be incremented only score will incremented
    public void wide(View view) {
        if (!gameEnd) {
            if (currently_playing == 'A') {
                scoreOfTeamA++;
                teamAExtras++;
            } else if (currently_playing == 'B') {
                scoreOfTeamB++;
                teamBExtras++;
            }
        }
        displaystatus();
    }

    //In case of a NoBall one run is awarded. But balls are not incremented
    public void noball(View view) {
        if (!gameEnd) {
            /*
            In case of no ball wasnoball flag needs to be set because if player gets out on
            very next ball after this then that out will not be counted.
            Hence no wicket will be incremented if we press NO and after than press WKT
             */
            wasnoball = true;
            if (currently_playing == 'A') {
                scoreOfTeamA++;
                teamAExtras++;
                if (tossWon == 'B' && target <= scoreOfTeamA) {
                    status = "Team A won by " + (10 - teamAWickets) + " wickets";
                    gameEnd = true;
                }
            } else if (currently_playing == 'B') {
                scoreOfTeamB++;
                teamBExtras++;
                if (tossWon == 'A' && target <= scoreOfTeamA) {
                    status = "Team B won by " + (10 - teamBWickets) + " wickets";
                    gameEnd = true;
                }
            }
        }
        displaystatus();
    }


    public void wicket(View view) {
        /*is previous ball was not a no ball and game is not already ended
        then increment wicket upto maximum of 10, if 10 is reached then either target is set (batting first team)
        or game lost (target not reached as all players are out)

         */
        if (!wasnoball && !gameEnd) {
            if (currently_playing == 'A') {
                if (++teamAWickets == 10) {
                    if (tossWon == 'A') {
                        status = "Team A all-out! Target is: " + (scoreOfTeamA + 1);
                        target = scoreOfTeamA + 1;
                        currently_playing = 'B';
                    } else {
                        status = "Team A lost game by " + (target - scoreOfTeamA) + " runs!";
                        gameEnd = true;
                    }
                }
            } else if (currently_playing == 'B') {
                if (++teamBWickets == 10) {
                    if (tossWon == 'B') {
                        status = "Team B all-out! Target is: " + (scoreOfTeamB + 1);
                        currently_playing = 'A';
                        target = scoreOfTeamB + 1;
                    } else {
                        status = "Team B lost game by " + (target - scoreOfTeamB) + " runs";
                        gameEnd = true;
                    }
                }
            }
        } else {
            wasnoball = false;
        }
        displaystatus();
    }

    /*This method will display current status and score of both teams
     Since all variables are made global hence it doesnot need any argument
      */
    private void displaystatus() {
        TextView a = findViewById(R.id.team_a_score);
        a.setText(scoreOfTeamA + "/" + teamAWickets);
        a = findViewById(R.id.team_b_score);
        a.setText(scoreOfTeamB + "/" + teamBWickets);
        a = findViewById(R.id.view_status);
        a.setText("Status: " + status);
        a = findViewById(R.id.a_extra);
        a.setText("Extras: " + teamAExtras);
        a = findViewById(R.id.a_overs);
        a.setText("Overs: " + teamAOvers + "." + teamABalls);
        a = findViewById(R.id.b_extra);
        a.setText("Extras: " + teamBExtras);
        a = findViewById(R.id.b_overs);
        a.setText("Overs: " + teamBOvers + "." + teamBBalls);
    }

    /* Only after reset or start of app will trigger the betting selection buttons
      which are "Team A" and "Team B" to trigger, once selected the button will not work in between
      a game until RESET is pressed.
       */
    public void team_a_toss(View view) {
        if (gameEnd) {
            tossWon = 'A';
            resetall();
            currently_playing = 'A';
            gameEnd = false;
            status = "Team A batting";
            displaystatus();
        }
    }

    public void team_b_toss(View view) {
        if (gameEnd) {
            tossWon = 'B';
            resetall();
            currently_playing = 'B';
            gameEnd = false;
            status = "Team B batting";
            displaystatus();
        }
    }

    public void reset(View view) {
        resetall();
        displaystatus();
    }

    //RESET button will reset all data to 0 and status flags to their initial state.
    private void resetall() {
        gameEnd = true;
        scoreOfTeamA = 0;
        scoreOfTeamB = 0;
        target = 0;
        teamAOvers = 0;
        teamBOvers = 0;
        teamABalls = 0;
        teamBBalls = 0;
        teamAExtras = 0;
        teamBExtras = 0;
        teamAWickets = 0;
        teamBWickets = 0;
        status = "RESET Done!";
    }

}
