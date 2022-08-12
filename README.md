# Simple_Thread
多线程入门(复习语法-复习自2019年学习JavaSE时的多线程语法)

遇到一个问题, 原本编译后的文件应该存在与target/classes中, 不知为何却生成在了项目根目录/${project.build.directory}/该目录下, 
百度后发现该${project.build.directory}是maven的一个参数,默认为target/, 那么在我没有配置该参数的情况下, 存放在target/中是正常的,
但是在我配置的情况下,编译后的文件却出现在了${project.build.directory}目录下, 我估计是maven的${project.build.directory}参数不知为何取不到值了,
所以就直接是参数的形式出现在了目录名上, 我估计的是当我配置该参数值为target/后应该没问题了, 但是很奇怪网上并没有讲解该参数如何配置的,
我尝试过`<properties><project><build><directory>target/</></></>`不行, `<properties><project-build-directory>target<properties/>`,
也不行, 后只能将项目删了重新new project, 逐解决. (加上写该文件耗时40分钟, 艹)
