package net.sourceforge.simcpux;


import java.util.Random;

import org.json.JSONObject;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PayActivity extends Activity {
	
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		
		api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
		
		
		Button appayBtn = (Button) findViewById(R.id.appay_btn);
		appayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android?"+new Random();
				Button payBtn = (Button) findViewById(R.id.appay_btn);
				payBtn.setEnabled(false);
				Toast.makeText(PayActivity.this, "��ȡ������...", Toast.LENGTH_SHORT).show();
		        try{
					byte[] buf = Util.httpGet(url);
					if (buf != null && buf.length > 0) {
						String content = new String(buf);
						Log.e("get server pay params:",content);
			        	JSONObject json = new JSONObject(content); 
						if(null != json && !json.has("retcode") ){
							PayReq req = new PayReq();
						   // req.appId = "wxf8b4f85f3a794e77";// ������appId
							req.appId			= "wxb4ba3c02aa476ea1";//appΨһ��ʾ
							req.partnerId		= json.getString("partnerid");//΢�ź�����Id
							req.prepayId		= json.getString("prepayid");//Ԥ֧��id
							req.nonceStr		= json.getString("noncestr");//�����
							req.timeStamp		= json.getString("timestamp");//ʱ���
							req.packageValue	= json.getString("package");//֧����ʽ
							req.sign			= json.getString("sign");//֧������ǩ��
							req.extData			= "app data"; // optional
							
							
							
							Toast.makeText(PayActivity.this, "��������֧��", Toast.LENGTH_SHORT).show();
							///api.registerApp(Constant.WX_APP_ID);
							
							api.sendReq(req);
						}else{
				        	Log.d("PAY_GET", "���ش���"+json.getString("retmsg"));
				        	Toast.makeText(PayActivity.this, "���ش���"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
						}
					}else{
			        	Log.d("PAY_GET", "�������������");
			        	Toast.makeText(PayActivity.this, "�������������", Toast.LENGTH_SHORT).show();
			        }
		        }catch(Exception e){
		        	Log.e("PAY_GET", "�쳣��"+e.getMessage());
		        	Toast.makeText(PayActivity.this, "�쳣��"+e.getMessage(), Toast.LENGTH_SHORT).show();
		        }
		        payBtn.setEnabled(true);
			}
		});		
		Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
		checkPayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
				Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
