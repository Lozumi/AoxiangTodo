// 用户相关接口封装
// 作者: 刘黎可
// 版本: 1.0.0

// 引入useFetch，假设在项目中已经引入
import { useFetch } from 'some-fetch-library'; // 请替换成实际使用的库

// 定义基础URL
export let baseUrl = 'http://localhost:20220';

// 定义发送请求的通用函数
const useSendRequest = (body: any) => {
    return useFetch(baseUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
};

// 导出AccountRequest类
export default new class AccountRequest {
    /**
     * 发起登录请求
     * @param account 用户账号
     * @param password 用户密码
     * @returns Promise
     */
    useLogin(account: string, password: string) {
        const requestBody = {
            content: JSON.stringify({
                '@class': 'shared.UserInfo',
                userName: '',
                account: account,
                password: password,
            }),
            requestType: 'UserLogin',
        };
        return useSendRequest(requestBody);
    }

    /**
     * 发起注册请求
     * @param userName 用户名
     * @param account 用户账号
     * @param password 用户密码
     * @returns Promise
     */
    useRegister(userName: string, account: string, password: string) {
        const requestBody = {
            content: JSON.stringify({
                '@class': 'shared.UserInfo',
                userName: userName,
                account: account,
                password: password,
            }),
            requestType: 'UserRegister',
        };
        return useSendRequest(requestBody);
    }

    /**
     * 发起用户登出请求
     * @returns Promise
     */
    userLogout() {
        const requestBody = {
            content: JSON.stringify(''),
            requestType: 'UserLogout',
        };
        return useSendRequest(requestBody);
    }

    /**
     * 发起获取当前用户请求
     * @returns Promise
     */
    getCurrentUser() {
        const requestBody = {
            content: JSON.stringify(''),
            requestType: 'GetCurrentUser',
        };
        return useSendRequest(requestBody);
    }
};
