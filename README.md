# Annoid
Annotation library for Android.

示例如下：

```Java

@ViewLayoutId(R.layout.m_edit)
public class EditActy extends AbsActivity implements OnClickListener {
    @ViewOnClick    // 当本类 implements OnClickListener, 那么直接加上本注解即可，不用参数
    @ViewId(R.id.m_edit_title_left_btn_back)
    private ImageButton mBtnBack;
    @ViewId(value = R.id.m_edit_magic_board, visibility = View.GONE)    // 多参数
    private MagicBoardView mMagicBoard;
    @ViewId(name = "m_edit_text", visibility = View.INVISIBLE)    // 对于库项目，R.id.xxx非final的情况下，可写name字符串
    private TextView mText;
    //...

    @Override
    @ViewOnClick(@Ids(R.id.m_edit_magic_board)) // 若前面没有加@ViewOnClick, 也可以写在onClick()上面，参数也可以像下面这样
    @ViewOnClick(@Ids({R.id.m_edit_title_left_btn_back, R.id.m_edit_magic_board}))
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.m_acty_btn_download:
            //...
        break;
    }

    @ViewOnClick(@Ids({R.id.m_edit_title_left_btn_back, R.id.m_edit_magic_board}))  // 或者可以写在任意自定义方法上面
    private void myOnClick(View v) {  // 这里也可以不用参数，像这样: private void myOnClick() {}
        //...
    }

    @ViewOnClick(@Ids({R.id.m_edit_title_left_btn_back, R.id.m_edit_magic_board}))  //甚至还可以这样，有没有觉得很cool
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //...
        }
    };
}
```
* 请注意 `@ViewOnClick` 的用法非常灵活。
