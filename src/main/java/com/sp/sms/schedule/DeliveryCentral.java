/**
 * 
 */
package com.sp.sms.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.sms.DeliveryStation;

/**
 * @author sunpeng
 * 
 */
public class DeliveryCentral {

	private static final Logger logger = LoggerFactory
			.getLogger(DeliveryCentral.class);
	private DeliveryStation deliveryStation;
	private MessageSource messageSource;
	private Integer deliveryThreadCount = 1;
	private Long deliveryInterval = 100l; // 毫秒;
	private Boolean shutdown = false;

	public DeliveryCentral() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown = true;
				if (deliveryStation != null) {
					deliveryStation.shutdown();
				}
				if (messageSource != null) {
					messageSource.close();
				}
			}
		});
	}

	public void startup() {

		for (int i = 0; i < this.deliveryThreadCount; i++) {
			Thread thread = new Thread(new Deliveryman(),
					"Sms-Delivery-Thread-" + i);
			thread.setDaemon(true);
			thread.start();
		}
	}

	public DeliveryStation getDeliveryStation() {
		return deliveryStation;
	}

	public void setDeliveryStation(DeliveryStation deliveryStation) {
		this.deliveryStation = deliveryStation;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Integer getDeliveryThreadCount() {
		return deliveryThreadCount;
	}

	public void setDeliveryThreadCount(Integer deliveryThreadCount) {
		this.deliveryThreadCount = deliveryThreadCount;
	}

	public Long getDeliveryInterval() {
		return deliveryInterval;
	}

	public void setDeliveryInterval(Long deliveryInterval) {
		this.deliveryInterval = deliveryInterval;
	}

	protected class Deliveryman implements Runnable {
		@Override
		public void run() {
			while (!shutdown) {
				ShortMessage message = messageSource.get();
				if (message != null) {
					try {
						deliveryStation.deliver(message.getContent(),
								message.getTargets());
					} catch (Exception e) {
						logger.error("Deliver message error.", e);
					}
				} else {
					try {
						Thread.sleep(deliveryInterval);
					} catch (InterruptedException e) {
						// ignore
					}
				}
			}

		}

	}

}
