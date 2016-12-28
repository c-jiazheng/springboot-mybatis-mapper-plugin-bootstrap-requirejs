package com.zyf.other.websocket.utils;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengyufei on 2016/11/24.
 */
public class WebSocketUtils {

	private static Map<String, ArrayList<WebSocketSession>> groups;

	static {
		groups = new HashMap<>();
	}

	public static ArrayList<WebSocketSession> getAllUsers() {
		ArrayList<WebSocketSession> allUsers = new ArrayList<>();
		//groups.forEach((k, v) -> allUsers.addAll(v));
		return allUsers;
	}

	public static ArrayList<WebSocketSession> getUsers(WebSocketSession session) {
		String namespace = getNamespace(session);
		ArrayList<WebSocketSession> users;
		if (!groups.containsKey(namespace))
			groups.put(namespace, users = new ArrayList<>());
		else
			users = groups.get(namespace);
		return users;
	}

	public static String getNamespace(WebSocketSession session) {
		String uri = session.getUri().getPath();
		return uri.substring(1, uri.indexOf("/", uri.indexOf("/", 1) + 1));
	}

	/**
	 * 给所有在线用户发送消息
	 *
	 * @param message
	 */
	public static void sendMessageToAllUsers(String message) {
		for (WebSocketSession user : getAllUsers()) {
			try {
				if (user.isOpen())
					user.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给组内所有用户发送消息
	 *
	 * @param message
	 */
	public static void sendMessageToGroup(String namespace, String message) {
		ArrayList<WebSocketSession> groupAllUsers = groups.get(namespace);
		for (WebSocketSession user : groupAllUsers) {
			try {
				if (user.isOpen()) {
					user.sendMessage(new TextMessage(message));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给某个用户发送消息
	 *
	 * @param userName
	 * @param message
	 */
	public static void sendMessageToUser(String userName, String message) {
		for (WebSocketSession user : getAllUsers()) {
			if (user.getId().equals(userName)) {
				try {
					if (user.isOpen()) {
						user.sendMessage(new TextMessage(message));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
