// 番茄钟相关接口封装
// 作者: 刘黎可
// 版本: 1.0.0

// 引入useFetch，假设在项目中已经引入
import { useFetch } from 'some-fetch-library'; // 请替换成实际使用的库

// 定义基础URL
let baseUrl = 'http://localhost:20220';

// 定义发送请求的通用函数
const sendRequest = (body: any) => {
    return useFetch(baseUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
};

// 导出PomodoroRequest类
export default new class PomodoroRequest {
    /**
     * 发起开始番茄钟请求
     * @param innerId 内部标识符
     * @returns Promise
     */
    start(innerId: number) {
        const requestBody = {
            content: innerId,
            requestType: 'StartPomodoro',
        };
        return sendRequest(requestBody);
    }

    /**
     * 发起结束番茄钟请求
     * @returns Promise
     */
    end() {
        const requestBody = {
            content: '',
            requestType: 'EndPomodoro',
        };
        return sendRequest(requestBody);
    }

    /**
     * 发起编辑番茄钟请求
     * @param workTime 工作时间
     * @param restTime 休息时间
     * @returns Promise
     */
    edit(workTime: number, restTime: number) {
        const requestBody = {
            content: JSON.stringify({
                '@class': 'shared.PomodoroInfo',
                workTime: workTime,
                restTime: restTime,
            }),
            requestType: 'EditPomodoro',
        };
        return sendRequest(requestBody);
    }
};
