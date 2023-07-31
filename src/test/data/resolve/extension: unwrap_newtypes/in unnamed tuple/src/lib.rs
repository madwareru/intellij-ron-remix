struct MyRonStruct {
    foo: u32,
    bar: (NewType,),
}

struct NewType(MyInner);

struct MyInner {
  <ref>foo: u32,
}