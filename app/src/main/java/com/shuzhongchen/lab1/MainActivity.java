package com.shuzhongchen.lab1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemClock;
import android.os.Handler;

import com.shuzhongchen.lab1.utils.*;
import org.mariuszgromada.math.mxparser.*;

import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView timer, success_counter, skip_counter, attempt_counter, main_text_view;
    ImageButton skip_btn, reset_btn;
    Button num_btn1, num_btn2, num_btn3, num_btn4;
    Button add_btn, minus_btn, times_btn, divide_btn, left_bracket_btn, right_bracket_btn;
    Button clear_btn, submit_btn;

    int n1, n2, n3, n4;
    int attempt = 1, success = 0, skip = 0;

    String input = "";
    String solution = null;

    boolean started = false;

    Stack<Character> stack;
    Stack<Character> btnStack;

    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    private DrawerLayout mDrawerLayout;

    private Handler customHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customHandler = new Handler();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        initialization();

        reset(true, true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        initialNavigationDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset(boolean generateNum, boolean resetTimer) {

        if (resetTimer) {
            started = false;
            startTime = SystemClock.uptimeMillis();
        }

        while (!stack.empty()) {
            stack.pop();
        }

        while (!btnStack.empty()) {
            btnStack.pop();
        }

        main_text_view.setText(StackUtils.makeString(stack));

        Random rn = new Random();

        if (generateNum) {
            do {
                n1 = rn.nextInt(9) + 1;
                n2 = rn.nextInt(9) + 1;
                n3 = rn.nextInt(9) + 1;
                n4 = rn.nextInt(9) + 1;
                solution = CalUtils.getSolution(n1, n2, n3, n4);
            } while (solution == null);
        }

        num_btn1.setText(String.valueOf(n1));
        num_btn2.setText(String.valueOf(n2));
        num_btn3.setText(String.valueOf(n3));
        num_btn4.setText(String.valueOf(n4));

        num_btn1.setEnabled(true);
        num_btn2.setEnabled(true);
        num_btn3.setEnabled(true);
        num_btn4.setEnabled(true);

        submit_btn.setEnabled(false);

        updateAllTextValue();
    }

    private void updateAllTextValue() {
        num_btn1.setText(String.valueOf(n1));
        num_btn2.setText(String.valueOf(n2));
        num_btn3.setText(String.valueOf(n3));
        num_btn4.setText(String.valueOf(n4));

        success_counter.setText(String.valueOf(success));
        skip_counter.setText(String.valueOf(skip));
        attempt_counter.setText(String.valueOf(attempt));

        main_text_view.setText(String.valueOf(input));
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timer.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    private void initialNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        // close drawer when item is tapped
                        switch (menuItem.getItemId()) {
                            case R.id.show_me:
                                Dialog dialogShowMe = new Dialog(MainActivity.this);
                                dialogShowMe.setTitle("Show Me");
                                dialogShowMe.setContentView(R.layout.dialog_show_me);
                                TextView show_me_solution = dialogShowMe.findViewById(R.id.show_me_solution);
                                if (solution == null) {
                                    show_me_solution.setText("No solution for these numbers");
                                } else {
                                    show_me_solution.setText(solution);
                                }
                                dialogShowMe.show();
                                break;
                            case R.id.assign_num:
                                final Dialog dialogAssignNum = new Dialog(MainActivity.this);
                                dialogAssignNum.setTitle("Assign Number");
                                dialogAssignNum.setContentView(R.layout.dialog_assign_num);
                                final NumberPicker np1 = (NumberPicker) dialogAssignNum.findViewById(R.id.numberPicker1);
                                final NumberPicker np2 = (NumberPicker) dialogAssignNum.findViewById(R.id.numberPicker2);
                                final NumberPicker np3 = (NumberPicker) dialogAssignNum.findViewById(R.id.numberPicker3);
                                final NumberPicker np4 = (NumberPicker) dialogAssignNum.findViewById(R.id.numberPicker4);

                                np1.setMaxValue(9);
                                np1.setMinValue(1);
                                np1.setValue(n1);
                                np2.setMaxValue(9);
                                np2.setMinValue(1);
                                np2.setValue(n2);
                                np3.setMaxValue(9);
                                np3.setMinValue(1);
                                np3.setValue(n3);
                                np4.setMaxValue(9);
                                np4.setMinValue(1);
                                np4.setValue(n4);

                                np1.setWrapSelectorWheel(false);
                                np2.setWrapSelectorWheel(false);
                                np3.setWrapSelectorWheel(false);
                                np4.setWrapSelectorWheel(false);

                                Button btn = (Button)  dialogAssignNum.findViewById(R.id.assign_num_btn);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        n1 = np1.getValue();
                                        n2 = np2.getValue();
                                        n3 = np3.getValue();
                                        n4 = np4.getValue();

                                        solution = CalUtils.getSolution(n1, n2, n3, n4);

                                        if (started) {
                                            skip++;
                                        }

                                        reset(false, true);

                                        Toast.makeText(MainActivity.this, "The numbers have been reset",
                                                Toast.LENGTH_LONG).show();

                                        dialogAssignNum.dismiss();
                                        mDrawerLayout.closeDrawers();
                                    }
                                });
                                dialogAssignNum.show();
                                break;
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    private void initialization() {
        stack = new Stack<>();
        btnStack = new Stack<>();

        timer = (TextView)findViewById(R.id.timer);
        success_counter = (TextView)findViewById(R.id.success_counter);
        skip_counter = (TextView)findViewById(R.id.skip_counter);
        attempt_counter = (TextView)findViewById(R.id.attempt_counter);
        main_text_view = (TextView)findViewById(R.id.main_text_view);

        skip_btn = (ImageButton)findViewById(R.id.skip_btn);
        skip_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                skip++;
                reset(true, true);
            }
        });

        reset_btn = (ImageButton)findViewById(R.id.reset_btn);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset(false, false);
            }
        });

        num_btn1 = (Button)findViewById(R.id.num_btn1);
        num_btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler((char)(n1 + '0'));
                btnStack.add('1');
                num_btn1.setEnabled(false);
            }
        });

        num_btn2 = (Button)findViewById(R.id.num_btn2);
        num_btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler((char)(n2 + '0'));
                btnStack.add('2');
                num_btn2.setEnabled(false);
            }
        });

        num_btn3 = (Button)findViewById(R.id.num_btn3);
        num_btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler((char)(n3 + '0'));
                btnStack.add('3');
                num_btn3.setEnabled(false);
            }
        });

        num_btn4 = (Button)findViewById(R.id.num_btn4);
        num_btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler((char)(n4 + '0'));
                btnStack.add('4');
                num_btn4.setEnabled(false);
            }
        });

        add_btn = (Button)findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('+');
            }
        });

        minus_btn = (Button)findViewById(R.id.minus_btn);
        minus_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('-');
            }
        });

        times_btn = (Button)findViewById(R.id.times_btn);
        times_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('*');
            }
        });

        divide_btn = (Button)findViewById(R.id.divide_btn);
        divide_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('/');
            }
        });

        left_bracket_btn = (Button)findViewById(R.id.left_bracket_btn);
        left_bracket_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('(');
            }
        });

        right_bracket_btn = (Button)findViewById(R.id.right_bracket_btn);
        right_bracket_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler(')');
            }
        });

        clear_btn = (Button)findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('c');
            }
        });

        submit_btn = (Button)findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnHandler('s');
            }
        });

    }

    private void btnHandler(char c) {
        switch (c) {
            case 'c':
                if (!stack.empty() && Character.isDigit(stack.peek())) {
                    char btnLabel = btnStack.pop();
                    switch (btnLabel) {
                        case '1':
                            num_btn1.setEnabled(true);
                            break;
                        case '2':
                            num_btn2.setEnabled(true);
                            break;
                        case '3':
                            num_btn3.setEnabled(true);
                            break;
                        case '4':
                            num_btn4.setEnabled(true);
                            break;
                    }
                }
                if (!stack.empty()) {
                    started = true;
                    submit_btn.setEnabled(true);
                }
                main_text_view.setText(StackUtils.removeFromStack(stack));
                break;
            case 's':
                submit_btn.setEnabled(false);
                Expression e = new Expression(StackUtils.makeString(stack));
                mDrawerLayout = findViewById(R.id.drawer_layout);
                if (!e.checkSyntax()) {
                    Snackbar snackbar = Snackbar
                            .make(mDrawerLayout, "Syntax error!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    attempt++;
                } else {
                    if (Math.abs(e.calculate() - 24.0) < 0.0000001) {

                        final Dialog dialogBinggo = new Dialog(MainActivity.this);
                        dialogBinggo.setTitle("Binggo");
                        dialogBinggo.setContentView(R.layout.dialog_binggo);

                        TextView dialog_binggo_text = dialogBinggo.findViewById(R.id.dialog_binggo_text);
                        dialog_binggo_text.setText("Binggo! " + solution + "=24");

                        Button btn = (Button)  dialogBinggo.findViewById(R.id.dialog_binggo_button);
                        btn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                success++;
                                reset(true, true);
                                dialogBinggo.dismiss();
                            }
                        });

                        dialogBinggo.show();

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(mDrawerLayout, "Wrong answer!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        attempt++;
                    }
                }
                break;

            default:
                started = true;
                submit_btn.setEnabled(true);
                main_text_view.setText(StackUtils.addToStack(stack, c));
        }
    }

}
