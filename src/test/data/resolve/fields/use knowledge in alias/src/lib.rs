
type MyRonAlias = a::MyRonStruct;

mod a {
    struct Bar {
       <ref>baz: u32,
    }

    struct MyRonStruct {
        foo: u32,
        bar: Bar,
    }
}

struct MyRonStruct {
    foo: u32,
    bar: Bar,
}

struct Bar {
   baz: u32,
}
