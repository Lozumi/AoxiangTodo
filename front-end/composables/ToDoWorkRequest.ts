// 待办事项相关接口封装
// 作者: 刘黎可
// 版本: 1.0.0


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
    immediate: true,
    // getCachedData: (...key)=>{
    //   console.log('try to get cached by key',key,body);
    //   debugger
    //   return undefined
    // }
  });
};

// 创建一个待办事项对象
const createTodoWork = (
    workLayer: number,
    workInnerId: number,
    workImportancePriority: number,
    workEmergencyPriority: number,
    workTitle: string,
    workSubtitle: string,
    workDescription: string,
    workCreateTime: string,
    workStartTime: string,
    workDeadline: string,
    workStatus: string,
    workSubToDoWorkItemInnerIdList: any,
    workPomodoroRecordInnerIdList: any
) => {
  return JSON.stringify({
    '@class': 'shared.ToDoWorkItem',
    layer: workLayer,
    innerId: workInnerId,
    importancePriority: workImportancePriority,
    emergencyPriority: workEmergencyPriority,
    title: workTitle,
    subtitle: workSubtitle,
    description: workDescription,
    createTime: workCreateTime,
    startTime: workStartTime,
    deadLine: workDeadline,
    status: workStatus,
    subToDoWorkItemInnerIdList: workSubToDoWorkItemInnerIdList,
    pomodoroRecordInnerIdList: workPomodoroRecordInnerIdList,
  });
};

// 导出ToDoWorkRequest类
export default new class ToDoWorkRequest {
  /**
   * 发起创建待办事项请求
   * @param todoItem 待办事项对象
   * @returns Promise
   */
  create(todoItem: any) {
    const requestBody = {
      content: todoItem,
      requestType: 'CreateToDoWork',
    };
    return sendRequest(requestBody);
  }

  /**
   * 发起查询待办事项请求
   * @param innerId 待办事项内部标识符
   * @returns Promise
   */
  query(innerId: number) {
    const requestBody = {
      content: innerId,
      requestType: 'QueryToDoWork',
    };
    return sendRequest(requestBody);
  }

  /**
   * 发起删除待办事项请求
   * @param innerId 待办事项内部标识符
   * @returns Promise
   */
  delete(innerId: number) {
    const requestBody = {
      content: innerId,
      requestType: 'DeleteToDoWork',
    };
    return sendRequest(requestBody);
  }

  /**
   * 发起编辑待办事项请求
   * @param updatedTodoItem 更新后的待办事项对象
   * @returns Promise
   */
  edit(updatedTodoItem: any) {
    const requestBody = {
      content: updatedTodoItem,
      requestType: 'EditToDoWork',
    };
    return sendRequest(requestBody);
  }

  /**
   * 发起枚举待办事项列表请求
   * @returns Promise
   */
  enumerate() {
    const requestBody = {
      content: '',
      requestType: 'EnumerateToDoWorkList',
    };
    return sendRequest(requestBody);
  }
};
