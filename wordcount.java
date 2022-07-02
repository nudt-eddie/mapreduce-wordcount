import java.io.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class wordcount{

	public static void main(String args[]) throws Exception{
	
		Configuration conf = new Configuration();
		
		FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
		
		System.out.println("fileSystem:"+fs);


		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		

		if(otherArgs.length != 2){

			System.err.println("Usage: must provide input path and output path ");

			System.exit(2);

		}
		
		for (int i = 0; i < otherArgs.length; i=i+1)
		
			System.out.println("arg "+ i + ":" + otherArgs[i]);
		
		Job job = Job.getInstance(conf, "wordcount");
		
		job.setJarByClass(wordcount.class);
		
        job.setMapperClass(Map.class);
        
        job.setReducerClass(Reduce.class);
        
        job.setOutputKeyClass(Text.class);
        
        job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] values =line.split(" ");
			for (int i = 0; i < values.length; i=i+1){
				context.write(new Text(values[i]), new IntWritable(1));
			}
		}
	}

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int s = 0;
            for (IntWritable value : values) {
                s += value.get();
            }
            context.write(key, new IntWritable(s));
        }
    }
}
