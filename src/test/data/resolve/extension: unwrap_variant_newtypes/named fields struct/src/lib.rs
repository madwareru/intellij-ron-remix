struct MyRonStruct {
    foo: u32,
    bar: NewType,
}

enum MyRonEnum {
    MyRonStruct(MyInner),
}

struct MyInner {
  <ref>foo: u32,
}