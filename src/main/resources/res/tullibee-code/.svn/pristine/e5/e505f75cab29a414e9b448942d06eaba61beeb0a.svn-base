// Copyright 2010-2012 Christopher Redekop
// 
// This file is part of the Tullibee Resource Adapter.  The Tullibee Resource
// Adapter provides connectivity to Interactive Brokers through the Tullibee
// API, in accordance with the Java EE Connector Architecture.
// 
// The Tullibee Resource Adapter is free software: you can redistribute it
// and/or modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
// 
// The Tullibee Resource Adapter is distributed in the hope that it will be
// useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
// General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with the Tullibee Resource Adapter.  If not, see
// <http://www.gnu.org/licenses/>.

package net.sf.tullibee.resource;

import static java.lang.String.format;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

import javax.resource.spi.work.WorkException;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public class WrapperImpl implements EWrapper {

	private final ConnectionId connectionId;
	private final TullibeeResourceAdapter tullibeeResourceAdapter;
	private final DeliveryFactory deliveryFactory;
	private final ManagedConnectionImpl managedConnectionImpl;

	WrapperImpl(final ConnectionId connectionId,
			final TullibeeResourceAdapter tullibeeResourceAdapter,
			final DeliveryFactory deliveryFactory,
			final ManagedConnectionImpl managedConnectionImpl) {
		this.tullibeeResourceAdapter = tullibeeResourceAdapter;
		this.connectionId = connectionId;
		this.deliveryFactory = deliveryFactory;
		this.managedConnectionImpl = managedConnectionImpl;
	}

	public void accountDownloadEnd(final String accountName) {
	}

	public void bondContractDetails(final int reqId,
			final ContractDetails contractDetails) {
	}

	public void contractDetails(final int reqId,
			final ContractDetails contractDetails) {
	}

	public void contractDetailsEnd(final int reqId) {
	}

	public void currentTime(final long time) {
		scheduleDeliveries(CurrentTimeActivationSpec.class,
				new CurrentTimeMessage() {
					public long getCurrentTime() {
						return time;
					}

					public ConnectionId getConnectionId() {
						return connectionId;
					}
				});
	}

	public void deltaNeutralValidation(final int reqId,
			final UnderComp underComp) {
	}

	public void execDetails(final int reqId, final Contract contract,
			final Execution execution) {
	}

	public void execDetailsEnd(final int reqId) {
	}

	public void fundamentalData(final int reqId, final String data) {
	}

	public void historicalData(final int reqId, final String date,
			final double open, final double high, final double low,
			final double close, final int volume, final int count,
			final double WAP, final boolean hasGaps) {
	}

	public void managedAccounts(final String accountsList) {
	}

	public void nextValidId(final int orderId) {
	}

	public void openOrder(final int orderId, final Contract contract,
			final Order order, final OrderState orderState) {
	}

	public void openOrderEnd() {
	}

	public void orderStatus(final int orderId, final String status,
			final int filled, final int remaining, final double avgFillPrice,
			final int permId, final int parentId, final double lastFillPrice,
			final int clientId, final String whyHeld) {
	}

	public void realtimeBar(final int reqId, final long time,
			final double open, final double high, final double low,
			final double close, final long volume, final double wap,
			final int count) {
	}

	public void receiveFA(final int faDataType, final String xml) {
	}

	public void scannerData(final int reqId, final int rank,
			final ContractDetails contractDetails, final String distance,
			final String benchmark, final String projection,
			final String legsStr) {
	}

	public void scannerDataEnd(final int reqId) {
	}

	public void scannerParameters(final String xml) {
	}

	public void stopRequested() {
	}

	public void tickEFP(final int tickerId, final int tickType,
			final double basisPoints, final String formattedBasisPoints,
			final double impliedFuture, final int holdDays,
			final String futureExpiry, final double dividendImpact,
			final double dividendsToExpiry) {
	}

	public void tickGeneric(final int tickerId, final int tickType,
			final double value) {
	}

	public void tickOptionComputation(final int tickerId, final int field,
			final double impliedVol, final double delta,
			final double modelPrice, final double pvDividend) {
	}

	public void tickPrice(final int tickerId, final int field,
			final double price, final int canAutoExecute) {
	}

	public void tickSize(final int tickerId, final int field, final int size) {
	}

	public void tickSnapshotEnd(final int reqId) {
	}

	public void tickString(final int tickerId, final int tickType,
			final String value) {
	}

	public void updateAccountTime(final String timeStamp) {
	}

	public void updateAccountValue(final String key, final String value,
			final String currency, final String accountName) {
	}

	public void updateMktDepth(final int tickerId, final int position,
			final int operation, final int side, final double price,
			final int size) {
	}

	public void updateMktDepthL2(final int tickerId, final int position,
			final String marketMaker, final int operation, final int side,
			final double price, final int size) {
	}

	public void updateNewsBulletin(final int msgId, final int msgType,
			final String message, final String origExchange) {
	}

	public void updatePortfolio(final Contract contract, final int position,
			final double marketPrice, final double marketValue,
			final double averageCost, final double unrealizedPNL,
			final double realizedPNL, final String accountName) {
	}

	public void connectionClosed() {
	}

	public void error(final Exception e) {
		managedConnectionImpl.fireConnectionErrorOccured(e);
		
		scheduleDeliveries(EncodedErrorActivationSpec.class,
				new ExceptionErrorMessage() {
					public Exception getException() {
						return e;
					}

					public ConnectionId getConnectionId() {
						return connectionId;
					}

					@Override
					public String toString() {
						return String.format(
								"%s [connectionId=%s, exception=%s", getClass()
										.getSimpleName(), connectionId, e);
					}
				});
	}

	private <M, L extends MessageListener<M>> void scheduleDeliveries(
			final Class<?> activationSpecClass, final M message) {
		final MessageEndpointFactoryRegistry messageEndpointFactoryRegistry = tullibeeResourceAdapter
				.getMessageEndpointFactoryRegistry();
		final Lock messageEndpointFactoryRegistryReadLock = messageEndpointFactoryRegistry
				.readLock();

		try {
			messageEndpointFactoryRegistryReadLock.lock();
			final Collection<MessageEndpointFactoryKey> messageEndpointFactoryKeys = messageEndpointFactoryRegistry
					.getRegistrationKeys(activationSpecClass);

			for (final MessageEndpointFactoryKey messageEndpointFactoryKey : messageEndpointFactoryKeys) {
				tullibeeResourceAdapter
						.scheduleWork(deliveryFactory
								.createDelivery(
										message,
										messageEndpointFactoryRegistry
												.getRegisteredEndpointFactory(messageEndpointFactoryKey),
										managedConnectionImpl));
			}
		} catch (final WorkException e) {
			e.printStackTrace();
		} finally {
			messageEndpointFactoryRegistryReadLock.unlock();
		}
	}

	public void error(final String str) {
	}

	public void error(final int id, final int errorCode, final String errorMsg) {
		scheduleDeliveries(EncodedErrorActivationSpec.class,
				new EncodedErrorMessage() {
					public int getRequestId() {
						return id;
					}

					public String getErrorMessage() {
						return errorMsg;
					}

					public int getErrorCode() {
						return errorCode;
					}

					public ConnectionId getConnectionId() {
						return connectionId;
					}

					@Override
					public String toString() {
						return format(
								"%s [connectionId=%s, requestId=%s, errorCode=%s, errorMessage=%s]",
								getClass().getSimpleName(), connectionId, id,
								errorCode, errorMsg);
					}
				});
	}
}
