实验四、Android数据存储

1、实验目的和要求

(1) 了解SharedPreferences轻量级的数据存储方式。

(2) 了解文件存储的内部存储与外部存储；外部分存储分为私有的和公有的两个存储空间。

(3) 掌握SQLite数据库的原理，使用SQL语句进行数据的增删改查操作。

(4) 了解Content Providers用于在不同应用之间共享数据。

2、实验预习

Android 数据存储是Android开发中的重要内容，涉及到如何在设备上保存和检索数据。以下是关于Android数据存储的一些主要知识点：

(1)   SharedPreferences：轻量级的数据存储方式，适用于保存少量数据，如配置参数。

特点：简单易用，占用空间小，但不适合存储大量数据。

(2)   文件存储：内部存储与外部存储，内部存储是私有的，只能由应用程序自身访问；外部存储是公共的，可以被其他应用访问,访问时需要设置权限。使用File类和相关IO操作类（如FileInputStream、FileOutputStream）进行文件的读写操作。

(3)   SQLite数据库：适用于结构化数据的存储，如用户信息、产品列表等。

通过SQLiteOpenHelper类创建和操作数据库，使用SQL语句进行数据的增删改查操作。

特点：性能较高，支持事务处理和查询优化。

(4)    Content Providers：用于在不同应用之间共享数据。定义一个Content Provider类来暴露数据，其他应用通过ContentResolver类来访问这些数据。

特点：安全、灵活，支持数据的共享和访问控制。

  

3、实验内容

1.修改前面的实验(Activity + Fragment这个实验),实现数据永久保存到数据库。即输入的数据保存到SqLite数据库中；界面上显示数据时从SqLite查询。详细如下：

点击“学生一览”， Fragment中显示学生一览画面，数据从数据库中检索过来显示(如果数据库中内容为空，可以增加一些演示数据)。

点击“添加学生”，Fragment中显示学生信息添加画面，数据保存后，更新到数据库中。

点击“关于”， Fragment中显示关于画面。（没有变化）

点击“退出”，应用程序关闭。（没有变化）

在学生一览的一行上点击详细按钮， Fragment换成显示学生信息修改画面,数据从SqLite查询显示出来。点击保存，修改的数据保存到SqLite中。删除数据时从SqLite中直接删除。

前面实验的菜单功能也要拿过来，保持不变。

4、实验总结与思考

(1) SQLiteOpenHelper类的作用是什么？里面的有哪些方法，干什么用？

答：SQLiteOpenHelper类是Android中用来管理数据库创建和版本更新的辅助类。在我们的程序中，它被实现为StudentDbHelper类。这个类里面主要有三个方法：首先是构造方法StudentDbHelper(Context context)，它通过调用父类构造函数来指定数据库名字为student_manager.db，版本号为1；其次是onCreate(SQLiteDatabase db)方法，它在数据库第一次创建时被系统调用，执行了创建学生表的SQL建表语句；最后是onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)方法，它在数据库版本升级时被调用，通过先删除旧表再调用onCreate来重新建表。

(2) 数据库操作(增删改查)一般定义成DAO操作类，程序中的操作类是什么？里面有哪些方法，这些方法分别调用了什么SQL语句？

答：程序中的数据库操作类是StudentDao。这个类中定义了几个核心的方法来完成增删改查：insert方法用于插入新学生，底层调用的是db.insert，对应的SQL语句为INSERT INTO students；update方法用于更新学生信息，底层调用的是db.update，对应的SQL语句是UPDATE students SET ... WHERE id = ?；delete方法用于删除学生记录，底层调用的是db.delete，对应的SQL语句是DELETE FROM students WHERE id = ?；findById方法用于根据ID查找单个学生，底层调用的是db.query，对应的SQL语句是SELECT * FROM students WHERE id = ?；findAll方法用于获取所有学生列表，底层调用的是db.query并传入id DESC进行降序排列，对应的SQL语句是SELECT * FROM students ORDER BY id DESC；count方法用于统计学生总人数，底层调用的是db.rawQuery，对应的SQL语句是SELECT COUNT(*) FROM students。

(3) 学生一览表中的listview使用的适配器(继承BaseAdapter的类)作了哪些修改？里面如何调用DAO类的方法的?

答：在学生一览表中，ListView使用的适配器是StudentAdapter类。我们在这个类中继承了BaseAdapter，并重写了getCount、getItem、getItemId和getView这四个核心方法。在getCount中返回了学生列表的长度，在getItem和getItemId中分别返回了对应位置的学生对象和它的数据库ID。在getView方法中，我们通过LayoutInflater加载了自定义的布局文件item_student.xml，把学生的学号、姓名、专业等信息绑定到界面控件上，并为详细按钮绑定了点击事件。关于如何调用DAO方法，StudentAdapter内部并没有直接调用StudentDao的方法，而是定义了一个OnDetailClickListener点击事件的回调接口。当用户点击详细按钮时，适配器会触发这个接口的onDetailClick方法，把点击事件交由外部的StudentListFragment处理。Fragment在收到回调后会跳转到编辑表单页面，最后在StudentFormFragment中通过调用StudentRepository间接调用了StudentDao的增删改查方法。

(4) 界面上增删改查访问数据时(包括listview的适配器类中访问数据时)，需要访问DAO操作类，为什么不在界面上直接调用DAO操作类，而是调用一个学生数据相关的类，在这个类中再调用DAO操作类？

答：不在界面上直接调用DAO操作类，而是通过调用学生数据相关的仓储类StudentRepository来间接调用DAO，主要是为了实现界面与数据的解耦和关注点分离。UI界面应该只负责展示数据和响应用户操作，而不应该关心数据具体是怎么存储的。引入Repository后，如果以后需要把数据存储方式从本地数据库改为网络API或加入内存缓存，我们只需要修改Repository内部的逻辑，而不需要动任何界面的代码，这大大提高了代码的可维护性。此外，Repository还可以用来封装一些通用的业务逻辑或默认的初始化操作，比如在我们的程序中，当数据库为空时，StudentRepository可以通过initDemoData方法自动插入几条默认的学生测试数据，这种初始化逻辑如果写在界面里就会显得很臃肿，写在仓储类里则更符合分层架构的设计原则。