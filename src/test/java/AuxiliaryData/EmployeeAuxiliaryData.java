package AuxiliaryData;

import net.minidev.json.JSONObject;
import org.json.JSONArray;
import ru.skypro.skypro_exercises_course5_hw3.entity.Employee;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;


public class EmployeeAuxiliaryData {
    public static JSONObject returnJsonEmployeeDTO(int number) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", number);
        jsonObject.put("name", Integer.toString(number));
        jsonObject.put("salary", number);
        jsonObject.put("position", number);
        return jsonObject;
    }

    public static String returnJsonEmployeeDTOList(int... numbers) {
        JSONArray jsonArray = new JSONArray();
        for (int number : numbers) {
            jsonArray.put(returnJsonEmployeeDTO(number));
        }
        return jsonArray.toString()
                .replace("[\"", "[")
                .replace("\",\"", ",")
                .replace("\\", "")
                .replace("\"]", "]");
    }

    public static List<Employee> returnEmployeeListFromDB(EmployeeRepository repository) {
        List<Employee> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }
}
