function addTableRow() {
    var table=document.getElementById("table")
    // console.log(table);
    var length=table.rows.length;
    // console.log(length);
    var newRow=table.insertRow(length);
    console.log(newRow);
    //newRow.innerHTML = '1234567';

    //插入列节点对象
    var namecell=newRow.insertCell(0);
    var phonecell=newRow.insertCell(1);
    var actioncell=newRow.insertCell(2);
    
    //修改节点文本内容
    namecell.innerHTML = "未命名";
    phonecell.innerHTML = "xxxxxxxx";
    actioncell.innerHTML = '<button onclick="editRow(this)">编辑</button> <button onclick="deleteRow(this)">删除</button>';
}
    //删除数据函数
    function deleteRow(button){
        // console.log(button);
        var raw=button.parentNode.parentNode;
        // console.log(raw);
        raw.parentNode.removeChild(raw);
}
    //编辑数据函数
    function editRow(button){
        var raw=button.parentNode.parentNode;
        var namecell=raw.cells[0];
        var phonecell=raw.cells[1];

        var newname=prompt("请输入新的姓名");
        var newphone=prompt("请输入新的电话");

        namecell.innerHTML=newname;
        phonecell.innerHTML=newphone;
}
