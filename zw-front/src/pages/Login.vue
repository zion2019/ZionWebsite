<template>
    <Window :windowSize="0.9" :buttons="buttons">
      <div class="edit-container">
        <el-form ref="form" 
        label-position="left"
        :rules="rules"
        :model="user">
          <div class="form-wrapper">
            <el-form-item label="账号:" prop="loginName">
                <el-input v-model="user.loginName" :maxlength="10" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="密码:" prop="password">
              <el-input v-model="user.password" type="password"></el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </Window>
  </template>
  
<script lang="ts" setup>
    import Window from '../components/Window.vue';
    import { ref, reactive } from 'vue';
    import {UserService} from '../api/api';
    import md5 from 'md5';
    import { ElMessage } from 'element-plus'
    import { useRouter } from 'vue-router';
    const router = useRouter();

        // 按钮定义
    const buttons = [
    {
        'type':'warning'
        ,'isPlain':true
        ,'label':'注册'
        ,'function':()=>{router.push('/register');}
      },
      {
        'type':'success'
        ,'label':'登录'
        ,'function':login
      },

    ]

    const user = reactive({
        loginName: "",
        password: ""
    });

    const form = ref(null);

    const rules = reactive({
        loginName: [
            { required: true, message: 'Please enter the login name' },
            { pattern: /^[a-zA-Z0-9]{1,10}$/, message: '10位以内英文～' }
        ],
        password: [
            { required: true, message: 'Please enter the password',trigger: 'blur'},
            { pattern: /^[a-zA-Z0-9]{8,12}$/, message: '仅支持8～10位英文or数字～' }
        ]
    })

    function login() {
        let {validate} = form.value as any 
        validate((valid:Boolean) => {
            if(valid) {
                // Basic base64(loginName+password)
                const base64Str = 'Basic '+btoa(user.loginName+':'+md5(user.password));
                localStorage.setItem('USER_TOKEN',base64Str);
                // 登陆
                UserService.login({
                    loginName:user.loginName,password: md5(user.password)
                }).then((res:any)=>{
                    // 写入token
                    localStorage.setItem('USER_TOKEN','Bearer '+res.data.token);
                    localStorage.setItem('Header_key_user_id',res.data.id);
                    ElMessage.success('登陆成功');
                    // 跳转
                    router.push('/');
                }).catch(err=>{
                    localStorage.removeItem('USER_TOKEN');
                    ElMessage.error(err);
                })
                
            }
        })
    }
</script>
  