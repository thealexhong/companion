package iocompanion.github.thealexhong.companion;

public class MainActivity2 {//extends ActionBarActivity implements View.OnTouchListener {

    /*
    private final String SERVICE = "00:16:53:46:59:8E";
    private EV3Connector ev3Connector;
    private boolean mConn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ev3Connector = new EV3Connector(SERVICE);
        Button mConnect = (Button)findViewById(R.id.btn_connect);
        Button mFwd = (Button)findViewById(R.id.btn_fwd);
        Button mBwd = (Button)findViewById(R.id.btn_bwd);
        Button mLeft = (Button)findViewById(R.id.btn_left);
        Button mRight = (Button)findViewById(R.id.btn_right);
        mConnect.setOnTouchListener(this);
        mFwd.setOnTouchListener(this);
        mBwd.setOnTouchListener(this);
        mLeft.setOnTouchListener(this);
        mRight.setOnTouchListener(this);
    }


    public boolean onTouch(View view, MotionEvent event)
    {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN)
        {
            switch(view.getId())
            {
                case R.id.btn_connect:
                    connect();
                    break;
                case R.id.btn_fwd:
                    forward();
                    break;
                case R.id.btn_bwd:
                    backward();
                    break;
                case R.id.btn_left:
                    left();
                    break;
                case R.id.btn_right:
                    right();
                    break;
            }
        }
        else
        if((action == MotionEvent.ACTION_UP) ||
                (action == MotionEvent.ACTION_CANCEL))
        {
            if(mConn) {
                ev3Connector.halt();
            }
        }
        return true;
    }

    public void connect()
    {

        ev3Connector.setBluetooth(EV3Connector.BT_ON);
        if(ev3Connector.connect())
        {
            mConn = true;
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void forward()
    {
        ev3Connector.moveForward();
    }
    public void backward()
    {
        ev3Connector.moveBackward();

    }
    public void left()
    {
        ev3Connector.turnLeft();

    }
    public void right()
    {
        ev3Connector.turnRight();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
