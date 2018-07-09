package com.bairock.iot.eleInfo.communication;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.ValueTrigger;
import com.bairock.iot.eleInfo.ValueTrigger.OnTriggedChangedListener;
import com.bairock.iot.eleInfo.listener.StartUpListener;

public class MyOnTriggedChangedListener implements OnTriggedChangedListener {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	@Override
	public void onTriggedChanged(ValueTrigger trigger, boolean trigged) {
		logger.info(trigger.getInfo() + trigger.getCompareSymbol() + trigged);
		byte[] by;
		if(trigged) {
			//触发了
			byte action = 0;
			if(trigger.getTargetValue() == 1) {
				action = (byte) 0xff;
			}
			by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 04, action, 00};
			byte[] byMsg = StartUpListener.createByteMsg(by);
			if (null != byMsg) {
				logger.info(ServerHandler.bytesToHexString(byMsg));
				ServerHandler.send(byMsg);
			}
		}else {
		}
	}

}
