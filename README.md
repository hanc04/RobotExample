# Robot SDK 集成示例

这是一个仅调用了语音交互功能的Robot SDK的Demo应用，用于向开发者展示 RobotSDK 最有限且必要的的集成步骤和最终效果。
在开发者开始基于自身项目进行正式的开发之前，请先通过这个项目验证自己是否已经正确集成了Robot SDK。

## How To Use This Repo:

### Step 1. 

首先联系我们获取 SDK aar文件


### Step 2. 

打开项目**app** module 的 build.gradle文件；

1. 添加开发者自己的keystore文件（即注册Robot SDK时提供的签名文件MD5所对应的keystore）以及相关配置到signingConfigs节点
2. 将开发者自己的applicationId（即注册Robot SDK时提供的APP包名）替换到build.gradle中
3. 将获取到的aar文件import到项目中


### Step 3.

运行项目，检查是否能正确完成语音交互
