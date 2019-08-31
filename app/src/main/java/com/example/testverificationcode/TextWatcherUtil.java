package com.example.testverificationcode;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * 输入框 手机号验证
 */
public class TextWatcherUtil {

    public interface Callback {
        void callback();
    }

    public static void addPhoneNumberTextWatcher(final EditText editText, final Callback callback) {
        if (editText == null) {
            return;
        }
        TextWatcher textWatcher = new TextWatcher() {

            private int start = 0;
            private int before = 0;
            private int count = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.before = before;
                this.count = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    filterPhoneNumber(s);
                } catch (Exception e) {
                }
            }

            public void filterPhoneNumber(Editable s) throws Exception {
                String temp = s.toString();
                int selection = editText.getSelectionStart();
                char[] arr = temp.toCharArray();
                boolean isMatch = true;

                if (count > 0) {
                    for (int i = start; i < start + count; i++) {
                        int c = arr[i];
                        if (!(c == 32 || (c >= 48 && c <= 57))) {//不是空格 并且 不是数字
                            isMatch = false;
                            break;
                        }
                    }
                }

                if (isMatch) {

                    boolean needReset = false;

                    if (arr.length >= 4) {
                        if (arr[3] != 32) {
                            needReset = true;
                        }
                    }
                    if (!needReset && arr.length >= 9) {
                        if (arr[8] != 32) {
                            needReset = true;
                        }
                    }

                    if (!needReset) {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i] == 32) {
                                if (i != 3 && i != 8) {
                                    needReset = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (needReset) {
                        temp = temp.replace(" ", "");
                        if (count > 0) {
                            if (temp.length() >= 7) {
                                s.clear();
                                s.append(temp.substring(0, 3) + " " + temp.substring(3, 7) + " " + temp.substring(7, temp.length()));
                                editText.setSelection(selection - 1);
                            } else if (temp.length() >= 3) {
                                s.clear();
                                s.append(temp.substring(0, 3) + " " + temp.substring(3, temp.length()));
                                editText.setSelection(selection - 1);
                            }
                        } else if (before > 0) {
                            if (selection == 3) {
                                s.clear();
                                if (temp.length() >= 8) {
                                    s.append(temp.substring(0, 2) + temp.substring(3, 4) + " " + temp.substring(4, 7) + " " + temp.substring(7, temp.length()));
                                } else if (temp.length() >= 4) {
                                    s.append(temp.substring(0, 2) + temp.substring(3, 4) + " " + temp.substring(4, temp.length()));
                                }
                                editText.setSelection(selection - 1);
                            } else if (selection == 8) {
                                s.clear();
                                if (temp.length() >= 8) {
                                    s.append(temp.substring(0, 3) + " " + temp.substring(3, 6) + " " + temp.substring(7, temp.length()));
                                }
                                editText.setSelection(selection - 1);
                            } else if (temp.length() >= 7) {
                                s.clear();
                                s.append(temp.substring(0, 3) + " " + temp.substring(3, 7) + " " + temp.substring(7, temp.length()));
                                editText.setSelection(selection);
                            } else if (temp.length() >= 3) {
                                s.clear();
                                s.append(temp.substring(0, 3) + " " + temp.substring(3, temp.length()));
                                editText.setSelection(selection);
                            }
                        }
                    } else {
                        if (count > 0) {
                            if (temp.length() == 3 || temp.length() == 8) {
                                s.append(" ");
                            }
                        } else if (before > 0) {
                            if (temp.length() == 3 || temp.length() == 8) {
                                s.delete(temp.length() - 1, temp.length());
                            }
                        }
                    }

                    if (callback != null) {
                        callback.callback();
                    }
                } else {
                    s.delete(start, start + count);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
    }

    public static void addPasswordTextWatcher(final EditText editText, final Callback callback) {
        if (null == editText) {
            return;
        }
        TextWatcher textWatcher = new TextWatcher() {

            private int start = 0;
            private int count = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String temp = s.toString();
                    char[] arr = temp.toCharArray();
                    boolean isMatch = true;

                    if (count > 0) {
                        for (int i = start; i < start + count; i++) {
                            int c = arr[i];
                            if (!((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122))) {//不是数字 && 不是小写字母 && 不是大写字母
                                isMatch = false;
                                break;
                            }
                        }
                    }

                    if (isMatch) {
                        if (callback != null) {
                            callback.callback();
                        }
                    } else {
                        s.delete(start, start + count);
                    }
                } catch (Exception e) {
                }
            }
        };

        editText.addTextChangedListener(textWatcher);

    }

    // 禁止复制粘贴
    public static void setDisenableCopyPaster(EditText editText) {
        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);
        editText.setCustomSelectionActionModeCallback(new android.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
            }
        });
    }
}
