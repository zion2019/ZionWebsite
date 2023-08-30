import { request } from './axios'

/**
 * @description -封装User类型的接口方法
 */
export class UserService {       // 模块一

    /**
     * @description 用户登录
     * @param {string} username - 用户名
     * @return {HttpResponse} result
     */
    static async login(params) {   // 接口一
        return request('/res/user/token', params)
    }

    /**
     * @description 用户注册
     * @param {string} username - 用户名
     * @return {HttpResponse} result
     */
    static async register(params) {   // 接口一
        return request('/res/user/register', params)
    }
    

}



/**
 * @description -封装File服务的接口方法
 */
export class FileService {       

    /**
     * @description 上传文件
     * @param {binary} file - 文件名
     * @return {HttpResponse} result
     */
    static async upload(rawFile) {   
        const formData = new FormData();
        formData.append('file', rawFile);
        return request('/res/file/upload', formData, 'POST')
    }

}

/**
 * @description -封装Topic服务的接口方法
 */
export class TopicService {      

    /**
     * @description 主题树
     * @return {HttpResponse} result
     */
    static async tree(excludeId) {
        let params = {excludeId:excludeId};
        return request('/learn/topic/tree',params,'GET')
    }

    /**
     * @description info
     * @return {HttpResponse} result
     */
    static async info(topicId) {
        if(topicId == null){
            return [];
        }
        return request('/learn/topic/info',{id:topicId},'GET')
    }

    /**
     * @description save
     * @return {HttpResponse} result
     */
     static async save(topic) {
        return request('/learn/topic',topic)
    }

    /**
     * @description page 
     * @return {HttpResponse} result
     */
    static async page(params) {
        return request('/learn/topic',params,'GET')
    }

    /**
     * @description remove 
     * @return {HttpResponse} result
     */
    static async remove(topicId) {
        if(topicId == null){
            return [];
        }
        return request('/learn/topic',{topicId},'DELETE');
    }

}

/**
 * @description -封装Point服务的接口方法
 */
export class PointService {      

    /**
     * @description save
     * @return {HttpResponse} result
     */
    static async save(point) {
        return request('/learn/point',point,'POST')
    }

    /**
     * @description info
     * @return {HttpResponse} result
     */
    static async info(pointId) {
        if(!pointId){
            return [];
        }
        return request('/learn/point/info',{pointId},'GET')
    }
    
    /**
     * @description page 
     * @return {HttpResponse} result
     */
    static async page(params) {
        return request('/learn/point',params,'GET')
    }

    /**
     * @description remove 
     * @return {HttpResponse} result
     */
    static async remove(pointId) {
        if(pointId == null){
            return [];
        }
        return request('/learn/point',{pointId},'DELETE');
    }
    


}

/**
 * @description -封装Practise服务的接口方法
 */
export class PracticeService {      
    /**
     * @description page 
     * @return {HttpResponse} result
     */
    static async today(params) {
        return request('/learn/practice/today',params,'GET')
    }

    /**
     * @description page 
     * @return {HttpResponse} result
     */
    static async getpoint(params) {
        return request('/learn/practice/point',params,'GET')
    }

    /**
     * @description practise 
     * @return {HttpResponse} result
     */
    static async practise(params) {
        return request('/learn/practice',params,'POST')
    }

}