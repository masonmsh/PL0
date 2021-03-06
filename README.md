# PL0
IDE+命令行

IDE包括：
- 编辑器+关键字提示
- 编译器
- 集成输出
- debug

命令行包括：
- 编辑器
- 编译器
- 控制台
- debug

## PL0文法
```
〈程序〉→〈分程序〉.
〈分程序〉→ [<常量说明部分>][<变量说明部分>][<过程说明部分>]〈语句〉
 <常量说明部分> → const<常量定义>{ ,<常量定义>};
 <常量定义> → <标识符>=<无符号整数>
 <无符号整数> → <数字>{<数字>}
 <变量说明部分> → var<标识符>{ ,<标识符>};
 <标识符> → <字母>{<字母>|<数字>}
 <过程说明部分> → <过程首部><分程序>;{<过程说明部分>}
 <过程首部> → procedure<标识符>;
 <语句> → <赋值语句>|<条件语句>|<当型循环语句>|<过程调用语句>|<读语句>|<写语句>|<复合语句>|<空>
 <赋值语句> → <标识符>:=<表达式>
 <复合语句> → begin<语句>{;<语句>}end
 <条件> → <表达式><关系运算符><表达式>|odd<表达式>
 <表达式> → [+|-]<项>{<加减运算符><项>}
 <项> → <因子>{<乘除运算符><因子>}
 <因子> → <标识符>|<无符号整数>|(<表达式>)
 <加减运符> → +|-
 <乘除运算符> → *|/
 <关系运算符> → =|#|<|<=|>|>=
 <条件语句> → if<条件>then<语句>
 <过程调用语句> → call<标识符>
 <当型循环语句> → while<条件>do<语句>
 <读语句> → read(<标识符>{,<标识符>})
 <写语句> → write(<表达式>{,<表达式>})
 <字母> → a|b|c…x|y|z
 <数字> → 0|1|2…7|8|9
```

***

### 例：一个Pl/0源程序及生成的目标代码：
```
const a=10;
var b,c;
procedure p;
begin
  c:=b+a
end;
begin
  read(b);
  while b#0 do
    begin
      call  p;
      write(2*c);
      read(b)
     end
end .
```
**目标代码**
```
2	int  0  3
3	lod  1  3
4	lit   0  10
5	opr  0  2
6	sto  1  4
7	opr  0  0
8	int  0  5
9	opr  0  16
10	sto  0  3
11	lod  0  3
12	lit   0  0
13	opr  0  9
14	jpc  0  24
15	cal  0  2
16	lit   0  2
17	lod  0  4
18	opr  0  4
19	opr  0  14
20	opr  0  15
21	opr  0  16
22	sto  0  3 
23	jmp  0  11
24	opr  0  0
```
