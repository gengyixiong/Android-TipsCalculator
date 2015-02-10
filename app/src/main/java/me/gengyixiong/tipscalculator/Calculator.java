package me.gengyixiong.tipscalculator;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Calculator extends Activity {

    public static final String STATE_SB     = "savedBill";
    public static final String STATE_TIP    = "savedTip";
    public static final String STATE_PEOPLE = "savedPeopleNumber";
    public static final String STATE_TEXTVIEW_VISIBILITY = "saveTextViewVisibility";

    int[] numberButtonsId = {
            R.id.number1,
            R.id.number2,
            R.id.number3,
            R.id.number4,
            R.id.number5,
            R.id.number6,
            R.id.number7,
            R.id.number8,
            R.id.number9,
            R.id.number0,
    };  //number input buttons id.
    Button[] numberButtons = new Button[numberButtonsId.length];    //number input buttons


    double tipRate      = 0.00;     //tip rate, select by radio button
    double peopleNumber = 0.0;      //how many people share this bill, selected by radio button

    double billMoney    = 0.00;     //bill money, showed in first text view. user input
    double tipMoney     = 0.00;     //tip money, showed in tip text view;
    double totalMoney   = 0.00;     //total money. showed in total text view
    double sharedMoney  = 0.0;      //shared money, showed in share text view

    StringBuffer sb = new StringBuffer("0");    //used to compose number to display in text view
    TextView billShow;      //show user input bill money
    TextView tipShow;       //show tip money
    TextView totalShow;     //show total money
    TextView shareShow;     //show share money
    TextView tipName;       //indicate tip
    TextView totalName;     //indicate total
    TextView shareName;     //indicate aa

    RadioGroup rgTipRate;   //select tip rate
    RadioGroup rgPeopleNumber;  //select people number
    Button clearButton;     //clear Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        billShow    = (TextView) findViewById(R.id.Bill_show);
        tipShow     = (TextView) findViewById(R.id.Tip_show);
        shareShow   = (TextView) findViewById(R.id.AA_show);
        totalShow   = (TextView) findViewById(R.id.Total_show);
        tipName     = (TextView) findViewById(R.id.Tip);
        totalName   = (TextView) findViewById(R.id.Total);
        shareName   = (TextView) findViewById(R.id.AA);

        rgTipRate   = (RadioGroup) findViewById(R.id.tip_rate);
        rgPeopleNumber = (RadioGroup) findViewById(R.id.number_of_people);

        if (savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
        }

        for (int i = 0; i < numberButtons.length;i++){
            numberButtons[i] = (Button) findViewById(numberButtonsId[i]);
            numberButtons[i].setOnClickListener(new numberButtonsListener());
        }       //get all the buttons

        clearButton = (Button) findViewById(R.id.clear);            //get clear button
        clearButton.setOnClickListener(new clearButtonListener());  //set clear button listener

        rgTipRate.check(R.id.tip_rate_16);      //check 16% tip rate as default;
        tipRate = 0.16;                         //default tipRate corresponding to default check;
        rgPeopleNumber.check(R.id.people3);     //check 3 people as default
        peopleNumber = 3;                       //default people number corresponding to default check;



        rgTipRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tip_rate_0:
                        tipRate = 0.00;
                        break;
                    case R.id.tip_rate_16:
                        tipRate = 0.16;
                        break;
                    case R.id.tip_rate_19:
                        tipRate = 0.19;
                        break;
                    case R.id.tip_rate_22:
                        tipRate = 0.22;
                        break;
                    default:
                        tipRate = 0.13;
                }
                updateTextView(sb, tipRate, peopleNumber);      //update all text view since rate input changed
            }
        });                                                     //Tip rate RadioButton OnCheckedChangeListener

        rgPeopleNumber.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.people2:
                        peopleNumber = 2;
                        break;
                    case R.id.people3:
                        peopleNumber = 3;
                        break;
                    case R.id.people4:
                        peopleNumber = 4;
                        break;
                    case R.id.people5:
                        peopleNumber = 5;
                        break;
                    default:
                        peopleNumber = 2;
                }
                updateTextView(sb, tipRate, peopleNumber);          //update text views since people input changed
            }
        });
    }

    public class numberButtonsListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Button b = (Button) v;                          //get the pressed button component
            String numberString = b.getText().toString();   //get the text on the pressed button
            makeAllTextViewVisible();
            int strLength = sb.length();
            if (strLength < 6) {                            //123.45 max number 6 orders and not 0.0
                sb.append(numberString);                    //append pressed button number to string every time when a button pressed.
                if (Double.parseDouble(sb.toString()) != 0) {
                    updateTextView(sb, tipRate, peopleNumber);
                                                            //update text views since bill number input changed
                }else{
                    sb = new StringBuffer("0");
                }
            }else{
                ;
            }
        }
    }

    public class clearButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            billMoney = 0.00;                               //bill money reset to 0
            tipMoney  = 0.00;                               //tip money reset to 0
            sb        = new StringBuffer("0");              //string buffer reset to 0
            updateTextView(sb, tipRate, peopleNumber);      //update text views since these resets

            rgTipRate.check(R.id.tip_rate_16);      //check 16% tip rate as default;
            tipRate = 0.16;                         //default tipRate corresponding to default check;
            rgPeopleNumber.check(R.id.people3);     //check 3 people as default
            peopleNumber = 3;                       //default people number corresponding to default check;
            makeAllTextViewInvisible();
        }
    }

    public void updateTextView(StringBuffer sb, double tipRate, double peopleNumber){

        billMoney       = Double.parseDouble(sb.toString())/100;          //turn string buffer sb to double, and divided by 100, so user don't need to worry about the decimal points.
        DecimalFormat f = new DecimalFormat("##.00");                     //round to 2 decimals
        tipMoney        = billMoney *tipRate;                             //calculate tip money
        tipMoney        = (double)Math.round(tipMoney * 100) / 100;       //round tip money to 2 decimals
        totalMoney      = billMoney + tipMoney;                           //calculate total money
        totalMoney      = (double)Math.round(totalMoney * 100) / 100;     //round total money to 2 decimals
        sharedMoney     = totalMoney /peopleNumber;                       //calculate share money
        sharedMoney     = (double)Math.round(sharedMoney * 100) / 100;    //round share money to 2 decimals

        if (Math.abs(billMoney - 0)<0.0001){                              //if bill money is 0
            billShow.setText("0.00");                                     //out put 0
        }else if(billMoney < 1){
            billShow.setText("0"+String.valueOf(f.format(billMoney)));    //now can show "0.10", not ".10"
        }else{
            billShow.setText(String.valueOf(f.format(billMoney)));        //out put calculated value
        }
        if (Math.abs(tipMoney -0)<0.0001) {                               //if tip money is 0
            tipShow.setText("0.00");                                      //out put 0.00, money format
        }else if(tipMoney < 1){
            tipShow.setText("0"+String.valueOf(f.format(tipMoney)));
        }else{
            tipShow.setText(String.valueOf(f.format(tipMoney)));          //out put calculated money
        }
        if (Math.abs(totalMoney - 0)<0.0001){
           totalShow.setText("0.00");
        }else if (totalMoney < 1){
            totalShow.setText("0" + String.valueOf(f.format(totalMoney)));
        }else{
            totalShow.setText(String.valueOf(f.format(totalMoney)));
        }
        if (Math.abs(sharedMoney - 0)<0.0001){
            shareShow.setText("0.00");
        }else if(sharedMoney < 1){
            shareShow.setText("0" + String.valueOf(f.format(sharedMoney)));
        }else{
            shareShow.setText(String.valueOf(f.format(sharedMoney)));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(STATE_TIP, tipRate);
        outState.putDouble(STATE_PEOPLE, peopleNumber);
        outState.putString(STATE_SB, sb.toString());
        outState.putInt(STATE_TEXTVIEW_VISIBILITY, billShow.getVisibility());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tipRate = savedInstanceState.getDouble(STATE_TIP);
        peopleNumber = savedInstanceState.getDouble(STATE_PEOPLE);
        sb = new StringBuffer(savedInstanceState.getString(STATE_SB));
        updateTextView(sb, tipRate, peopleNumber);
        if (savedInstanceState.getInt(STATE_TEXTVIEW_VISIBILITY) == 0){
            makeAllTextViewVisible();
        }
    }

    void makeAllTextViewVisible(){
        billShow.setVisibility(View.VISIBLE);
        totalShow.setVisibility(View.VISIBLE);
        shareShow.setVisibility(View.VISIBLE);
        tipShow.setVisibility(View.VISIBLE);
        tipName.setVisibility(View.VISIBLE);
        totalName.setVisibility(View.VISIBLE);
        shareName.setVisibility(View.VISIBLE);
    }

    void makeAllTextViewInvisible(){
        tipShow.setVisibility(View.INVISIBLE);
        billShow.setVisibility(View.INVISIBLE);
        totalShow.setVisibility(View.INVISIBLE);
        shareShow.setVisibility(View.INVISIBLE);
        tipName.setVisibility(View.INVISIBLE);
        totalName.setVisibility(View.INVISIBLE);
        shareName.setVisibility(View.INVISIBLE);
    }
}
