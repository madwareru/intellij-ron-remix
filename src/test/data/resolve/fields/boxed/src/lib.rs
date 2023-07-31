struct Wrapper(Box<MyRonStruct>);

struct MyRonStruct {
    <ref>foo: u32,
    bar: String,
}