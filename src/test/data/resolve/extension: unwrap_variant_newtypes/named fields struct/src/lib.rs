struct MyRonStruct {
    foo: u32,
    bar: MyRonEnum,
}

enum MyRonEnum {
    MyRonStruct(MyInner),
}

struct MyInner {
  <ref>foo: u32,
}