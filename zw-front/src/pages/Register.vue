<template>
    <Window :buttons="buttons">
      
      <div class="edit-container">
        <div class="form-wrapper" >
          <el-form ref="form" 
          label-position="left"
          label-width=“80px”
          :rules="rules"
          :model="user">
            <el-form-item label="头像:">
                <el-upload
                    class="avatar-uploader"
                    :action="uploadUrl"
                    :headers="headers"
                    method="post"
                    name="file"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                >
                    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item prop="loginName">
                <el-input v-model="user.loginName" placeholder="登陆账号" :maxlength="10" show-word-limit></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="user.password" placeholder="密码" :minimize="6" :maxlength="12" type="password" show-word-limit></el-input>
            </el-form-item>
            <el-form-item  prop="confirmPassword">
              <el-input v-model="user.confirmPassword" placeholder="确认密码" :minimize="6" :maxlength="12" type="password" show-word-limit></el-input>
            </el-form-item>
            <el-form-item prop="nickName">
              <el-input v-model="user.nickName" placeholder="昵称" :maxlength="10" show-word-limit></el-input>
            </el-form-item>
            <el-form-item  prop="email">
              <el-input v-model="user.email" placeholder="邮箱" type="email"></el-input>
            </el-form-item>
            <el-form-item prop="telephone">
              <el-input v-model="user.telephone" placeholder="手机" type="tel"></el-input>
            </el-form-item>
          
          </el-form>
        </div>
      </div>
    </Window>
</template>

<script lang="ts" setup>
    import Window from '../components/Window.vue';
    import { ref, reactive } from 'vue';
    import {UserService} from '../api/api';
    import axios from 'axios';
    import md5 from 'md5';
    import { ElMessage } from 'element-plus'
    import { useRouter } from 'vue-router';
    const router = useRouter();

    // 来到注册页面，首先删除用户缓存避免发生问题
    localStorage.removeItem('USER_TOKEN');

    // 按钮定义
    const buttons = [
      {
        'type':'warning'
        ,'isPlain':true
        ,'label':'HOME'
        ,'function':()=>{router.push({name:'Home'})}
      },
      {
        'type':'success'
        ,'label':'注册'
        ,'function':register
      }
    ]

    
    const form = ref(null);
    const user = reactive({
        loginName: "",
        nickName: "",
        email: "",
        telephone: "",
        password: "",
        confirmPassword:"",
        avatar:""
    });

    const rules = reactive({
        loginName: [
            { required: true, message: 'Please enter the login name' },
            { pattern: /^[a-zA-Z0-9]{1,10}$/, message: '登陆名称仅支持10位以内英文～' }
        ],
        password: [
            { required: true, message: 'Please enter the password',trigger: 'blur'},
            { pattern: /^[a-zA-Z0-9]{6,12}$/, message: '密码仅支持6～10位英文or数字～' }
        ],
        confirmPassword: [
            { required: true, message: 'Please enter the password',trigger: 'blur'},
            { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        nickName: [
            { required: true, message: 'Please enter the nickname' },
            { pattern: /^[a-zA-Z0-9\u4e00-\u9fa5]{1,10}$/, message: '10位以内～' }
        ],
        email: [
            { required: false, message: 'Please enter the email' },
            { type: 'email', message: 'Invalid email format' }
        ],
        telephone: [
            { required: false, message: 'Please enter the telephone number' },
            { pattern: /^\d{11}$/, message: 'Telephone number must be 11 digits' }
        ]
    })
    function validateConfirmPassword(rule, value, callback) {
        if (value !== user.password) {
            callback(new Error('两次输入密码不一致'));
        } else {
            callback();
        }
    }

    // 注册方法
    function register() {
        let {validate} = form.value as any 
        validate((valid:Boolean) => {
            if(valid) {
                UserService.register({
                    'loginName':user.loginName
                    ,'password':md5(user.password)
                    ,'nickName':user.nickName
                    ,'email':user.email
                    ,'telephone':user.telephone
                    ,'avatar':user.avatar
                }).then((res:any)=>{
                    ElMessage.success(res.message);
                    router.push('/login');
                }).catch(err=>{
                    ElMessage.error(err);
                })
                // toRegister
            }
        })
    }

    /** 文件上传相关 */
    import { Plus } from '@element-plus/icons-vue'
    import type { UploadProps } from 'element-plus'

    // 定义上传请求信息
    const  uploadUrl = axios.defaults.baseURL+'/res/file/upload';
    console.log('sadasdasda',uploadUrl);
    const userToken = localStorage.getItem("USER_TOKEN");
    const headers = {Authorization: userToken}

    // 上传成功后处理事项
    const imageUrl = ref('')
    const handleAvatarSuccess: UploadProps['onSuccess'] = (response, uploadFile ) => {
        imageUrl.value = URL.createObjectURL(uploadFile.raw!)  
        user.avatar = response.data.fileUrl;
    }

    // 上传前的校验
    const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
        const allowedFormats = ['jpg', 'jpeg', 'png'];
        const fileExtension = (rawFile.name?.split('.').pop() ?? '').toLowerCase();

        if (!allowedFormats.includes(fileExtension)) {
            ElMessage.error('Unsupported file format');
            return false;
        }

        if (rawFile.size / 1024 / 1024 > 2) {
            ElMessage.error('File size cannot exceed 2MB');
            return false;
        }

        return true;
    }
</script>
<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>