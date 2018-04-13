package kualian.dc.deal.application.callback;


/**
 * 类描述: 服务端回调
 * 创建人: chenyang
 * QQ: 454725164
 * 邮箱: inke88@163.com
 * 创建时间: 2016年03月28日 16时53分
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public abstract class ServerCallBack<T> {
	/**
	 * 开始回调
	 */
	public void onStart() {
	}

	/**
	 * 成功回调
	 *
	 * @param t
	 */
	public abstract void onSuccess(T t);

	/**
	 * 失败回调
	 *
	 */
	public abstract void onFailure();
}