package Hadoop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


import java.io.IOException;
import java.lang.Integer;
import java.util.StringTokenizer;
import java.util.TreeSet;

// >>> Don't Change
public class TopPopularLinks extends Configured implements Tool {
    public static final Log LOG = LogFactory.getLog(TopPopularLinks.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new TopPopularLinks(), args);
        System.exit(res);
    }

    public static class IntArrayWritable extends ArrayWritable {
        public IntArrayWritable() {
            super(IntWritable.class);
        }

        public IntArrayWritable(Integer[] numbers) {
            super(IntWritable.class);
            IntWritable[] ints = new IntWritable[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                ints[i] = new IntWritable(numbers[i]);
            }
            set(ints);
        }
    }
// <<< Don't Change

    @Override
    public int run(String[] args) throws Exception {
        // TODO
    	   Configuration conf = this.getConf();
           FileSystem fs = FileSystem.get(conf);
           Path tmpPath = new Path("/mp2/tmp");
           fs.delete(tmpPath, true);

           Job jobA = Job.getInstance(conf, "Link Count");
           jobA.setOutputKeyClass(IntWritable.class);
           jobA.setOutputValueClass(IntWritable.class);

           jobA.setMapperClass(LinkCountMap.class);
           jobA.setReducerClass(LinkCountReduce.class);

           FileInputFormat.setInputPaths(jobA, new Path(args[0]));
           FileOutputFormat.setOutputPath(jobA, tmpPath);

           jobA.setJarByClass(TopPopularLinks.class);
           jobA.waitForCompletion(true);

           Job jobB = Job.getInstance(conf, "Top Popular");
           jobB.setOutputKeyClass(IntWritable.class);
           jobB.setOutputValueClass(IntWritable.class);

           jobB.setMapOutputKeyClass(NullWritable.class);
           jobB.setMapOutputValueClass(IntArrayWritable.class);

           jobB.setMapperClass(TopLinksMap.class);
           jobB.setReducerClass(TopLinksReduce.class);
           jobB.setNumReduceTasks(1);

           FileInputFormat.setInputPaths(jobB, tmpPath);
           FileOutputFormat.setOutputPath(jobB, new Path(args[1]));

           jobB.setInputFormatClass(KeyValueTextInputFormat.class);
           jobB.setOutputFormatClass(TextOutputFormat.class);

           jobB.setJarByClass(TopPopularLinks.class);
           return jobB.waitForCompletion(true) ? 0 : 1;
    }

    public static class LinkCountMap extends Mapper<Object, Text, IntWritable, IntWritable> {
        // TODO
    	@Override
    	public void map(Object key, Text value, Context context) throws IOException, InterruptedException
    	{
    		String line = value.toString();
        	StringTokenizer str_token = new StringTokenizer(line, " /t:");
        	String str_source = str_token.nextToken();
        	int id_source = Integer.parseInt(str_source);
        	context.write(new IntWritable(id_source), new IntWritable(0));
        	String str_direct = "";
        	int id_direct = 0;
        	while( str_token.hasMoreTokens())
        	{
        		str_direct = str_token.nextToken();
        		id_direct = Integer.parseInt(str_direct);
        		context.write(new IntWritable(id_direct), new IntWritable(1));
        	}
    	}
    	
    }

    public static class LinkCountReduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
        // TODO
    	@Override
    	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    	{
    		int sum = 0;
        	for(IntWritable count : values)
        	{
        		sum = sum + count.get();
        	}
        	
    		context.write(key, new IntWritable(sum) );

    	}
    }

    public static class TopLinksMap extends Mapper<Text, Text, NullWritable, IntArrayWritable> {
        Integer N;
        private TreeSet<Pair<Integer, Integer>> count_to_link_map = new TreeSet<Pair<Integer, Integer>>();
        @Override
        protected void setup(Context context) throws IOException,InterruptedException {
            Configuration conf = context.getConfiguration();
            this.N = conf.getInt("N", 10);
        }
        // TODO
        @Override 
        public void map (Text key, Text value, Context context) throws IOException, InterruptedException
        {
      	  Integer count = Integer.parseInt(value.toString());
          Integer link = Integer.parseInt(key.toString());

          count_to_link_map.add(new Pair<Integer, Integer>(count, link));

          if (count_to_link_map.size() > N ) {
              count_to_link_map.remove(count_to_link_map.first());
          }
        }
        
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            // TODO
            for (Pair<Integer, Integer> item : count_to_link_map) {
                Integer[] ints = {item.second, item.first};
                IntArrayWritable val = new IntArrayWritable(ints);
                context.write(NullWritable.get(), val);
            }
        }
    }

    public static class TopLinksReduce extends Reducer<NullWritable, IntArrayWritable, IntWritable, IntWritable> {
        Integer N;
        private TreeSet<Pair<Integer, Integer>> count_to_link_reduce = new TreeSet<Pair<Integer, Integer>>();
        
        @Override
        protected void setup(Context context) throws IOException,InterruptedException {
            Configuration conf = context.getConfiguration();
            this.N = conf.getInt("N", 10);
        }
        // TODO
        @Override
        public void reduce( NullWritable key, Iterable<IntArrayWritable> values , Context context) throws IOException, InterruptedException
        {
        	for(IntArrayWritable value : values)
        	{
        		IntWritable[] part = (IntWritable[]) value.toArray();
        		Integer link_id = part[0].get();
        		Integer count  = part[1].get();
        		count_to_link_reduce.add(new Pair<Integer, Integer>(count, link_id));
        		
        		if( count_to_link_reduce.size() > N )
        			count_to_link_reduce.remove(count_to_link_reduce.first());
        	}
        	
        	for(Pair<Integer, Integer> pair : count_to_link_reduce)
        	{
        		context.write(new IntWritable(pair.second),new IntWritable(pair.first));
        	}
        }
    }
}

// >>> Don't Change
class Pair<A extends Comparable<? super A>,
        B extends Comparable<? super B>>
        implements Comparable<Pair<A, B>> {

    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A extends Comparable<? super A>,
            B extends Comparable<? super B>>
    Pair<A, B> of(A first, B second) {
        return new Pair<A, B>(first, second);
    }

    @Override
    public int compareTo(Pair<A, B> o) {
        int cmp = o == null ? 1 : (this.first).compareTo(o.first);
        return cmp == 0 ? (this.second).compareTo(o.second) : cmp;
    }

    @Override
    public int hashCode() {
        return 31 * hashcode(first) + hashcode(second);
    }

    private static int hashcode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        if (this == obj)
            return true;
        return equal(first, ((Pair<?, ?>) obj).first)
                && equal(second, ((Pair<?, ?>) obj).second);
    }

    private boolean equal(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
// <<< Don't Change