package com.mayikt.weixin.mp.config;

import java.util.List;

import com.mayikt.weixin.mp.utils.JsonUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;



import lombok.Data;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpProperties {
	private List<MpConfig> configs;

	@Data
	public static class MpConfig {
		/**
		 * 设置微信公众号的appid
		 */
		private String appId;

		/**
		 * 设置微信公众号的app secret
		 */
		private String secret;

		/**
		 * 设置微信公众号的token
		 */
		private String token;

		/**
		 * 设置微信公众号的EncodingAESKey
		 */
		private String aesKey;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
