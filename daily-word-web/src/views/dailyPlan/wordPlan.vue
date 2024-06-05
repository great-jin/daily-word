<template>
  <el-dialog
      v-model="dialogVisible"
      width="40%"
      title="新增计划"
      style="--el-dialog-padding-primary: 15px;"
      :center="true"
  >
    <el-form
        ref="planForm"
        :rules="rules"
        :model="planForm"
        label-width="100px"
    >
      <el-form-item label="背诵词典:" prop="catalogue">
        <el-row style="width: 100%" :gutter="10">
          <el-col :span="18">
            <el-select
                v-model="planForm.catalogue"
                placeholder="选择词典"
                style="width: 100%"
                @change="onChange('dict', $event)"
            >
              <el-option label="CET4" value="CET4"/>
              <el-option label="CET6" value="CET6"/>
              <el-option label="GRE" value="GRE"/>
              <el-option label="考研词典" value="Graduate"/>
              <el-option label="牛津词典" value="Oxford"/>
            </el-select>
          </el-col>
          <el-col :span="6">
            <div style="line-height: 32px; color: grey">
              共计 {{ dictInfo.wordCount }} 个单词
            </div>
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item label="每日数量:" prop="size">
        <el-row style="width: 100%" :gutter="10">
          <el-col :span="18">
            <el-select
                v-model="planForm.size"
                placeholder="选择数量"
                style="width: 100%"
                @change="onChange('num', $event)"
            >
              <el-option
                  v-for="it in [10, 20, 30, 50]"
                  :label="`${it}个/组`"
                  :value="it"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <div style="line-height: 32px; color: grey">
              预计 {{ dictInfo.predictDay }} 天完成
            </div>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <div class="button-container">
        <el-button @click="handleOK('cancel')">取 消</el-button>
        <el-button type="primary" @click="handleOK('ok')">确 定</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import {getDictDetail} from "@/api/wordApi";

export default {
  data() {
    return {
      dialogVisible: false,
      dictDetail: [],
      predictDay: 0,
      dictInfo: {
        wordCount: 0,
        predictDay: 0,
      },
      planForm: {
        catalogue: '',
        size: ''
      },
      rules: {
        catalogue: [
          {required: true, message: '用户名不能为空', trigger: 'blur'},
        ],
        size: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      getDictDetail().then(res => {
        this.dictDetail = res.data
      })
    },
    clickOption(type) {
      if (type === 'ok') {

      } else {
        this.dialogVisible = false
      }
    },
    onChange(type, data) {
      const _catalog = this.planForm.catalogue
      if (_catalog === null || _catalog === '') {
        return
      }

      const item = this.dictDetail.filter(it => it.catalogue === _catalog)[0]
      const wordCount = item.wordCount
      if (type === 'num') {
        this.dictInfo.predictDay = Math.ceil(wordCount / data)
      } else {
        this.dictInfo.wordCount = wordCount
      }
    },
    handleOK(type) {
      if (type === 'ok') {
        this.$refs.planForm.validate(valid => {
          if (valid) {
            // 表单验证通过，执行提交逻辑
            // 处理注册逻辑
            this.$message.success(this.operate)
          } else {
            // 表单验证失败
            this.$message.error('表单验证失败')
          }
        })
      } else {
        this.dialogVisible = false
        this.planForm = {}
        this.$refs.planForm.clearValidate()
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  height: 100%;
}

.button-container {
  margin-bottom: 20px; /* 调整底部间距 */
}
</style>
